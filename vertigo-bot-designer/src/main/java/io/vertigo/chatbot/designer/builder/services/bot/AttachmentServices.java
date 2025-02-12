package io.vertigo.chatbot.designer.builder.services.bot;

import io.vertigo.account.authorization.annotations.SecuredOperation;
import io.vertigo.chatbot.commons.LogsUtils;
import io.vertigo.chatbot.commons.dao.AttachmentDAO;
import io.vertigo.chatbot.commons.domain.Attachment;
import io.vertigo.chatbot.commons.domain.AttachmentExport;
import io.vertigo.chatbot.commons.domain.AttachmentFileInfo;
import io.vertigo.chatbot.commons.domain.Chatbot;
import io.vertigo.chatbot.commons.multilingual.attachment.AttachmentMultilingualResources;
import io.vertigo.chatbot.designer.builder.services.AntivirusServices;
import io.vertigo.chatbot.designer.commons.services.FileServices;
import io.vertigo.chatbot.domain.DtDefinitions;
import io.vertigo.commons.transaction.Transactional;
import io.vertigo.core.lang.VUserException;
import io.vertigo.core.node.component.Activeable;
import io.vertigo.core.node.component.Component;
import io.vertigo.core.param.Param;
import io.vertigo.core.param.ParamManager;
import io.vertigo.datamodel.criteria.Criterions;
import io.vertigo.datamodel.structure.model.DtList;
import io.vertigo.datamodel.structure.model.DtListState;
import io.vertigo.datamodel.structure.util.VCollectors;
import io.vertigo.datastore.filestore.model.FileInfoURI;
import io.vertigo.datastore.filestore.model.VFile;
import xyz.capybara.clamav.commands.scan.result.ScanResult;

import javax.inject.Inject;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Base64;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static io.vertigo.chatbot.designer.utils.ListUtils.MAX_ELEMENTS_PLUS_ONE;

@Transactional
public class AttachmentServices implements Component, Activeable {

	@Inject
	private AttachmentDAO attachmentDAO;

	@Inject
	private FileServices fileServices;

	@Inject
	private AntivirusServices antivirusServices;

	@Inject
	private ParamManager paramManager;

	private String[] extensionsWhiteList;

	@Override
	public void start() {
		extensionsWhiteList = paramManager.getOptionalParam("EXTENSIONS_WHITELIST")
				.map(Param::getValueAsString).orElse("png,jpg,jpeg,pdf").split(",");
	}

	public Attachment findById(final long attachmentId) {
		return attachmentDAO.get(attachmentId);
	}

	public Attachment save(final Attachment attachment, final Optional<FileInfoURI> optFileInfoURI, final Long maxSize, final Long attachmentTotalSize) {

		if (attachment.getAttId() == null && optFileInfoURI.isEmpty()) {
			throw new VUserException(AttachmentMultilingualResources.MUST_CONTAINS_A_FILE);
		}

		if (optFileInfoURI.isPresent()) {
			Long oldAttachmentFileId = attachment.getAttFiId();
			Long oldAttachmentSize = 0L;
			if (attachment.getAttId() != null) {
				final Attachment oldAttachment = findById(attachment.getAttId());
				oldAttachmentSize = oldAttachment.getLength();
				oldAttachmentFileId = oldAttachment.getAttFiId();
			}
			final VFile newAttachmentFile = fileServices.getFileTmp(optFileInfoURI.get());
			final String newAttachmentFileMimeType = newAttachmentFile.getMimeType();
			if (Arrays.stream(extensionsWhiteList).noneMatch(newAttachmentFileMimeType::contains)) {
				throw new VUserException(AttachmentMultilingualResources.EXTENSION_NOT_ALLOWED, newAttachmentFileMimeType,
						String.join(",", extensionsWhiteList));
			}

			try {
				final ScanResult result = antivirusServices.checkForViruses(newAttachmentFile.createInputStream());
				if (result instanceof ScanResult.VirusFound) {
					final Map<String, Collection<String>> virusesMap = ((ScanResult.VirusFound) result).getFoundViruses();
					final String viruses = virusesMap.values().stream()
							.flatMap(Collection::stream).collect(Collectors.joining(","));
					throw new VUserException(AttachmentMultilingualResources.VIRUSES_FOUND, viruses);
				}
			} catch (final IOException ioException) {
				throw new VUserException(AttachmentMultilingualResources.COULD_NOT_OPEN_FILE, newAttachmentFile.getFileName(), ioException);
			}

			if (isMaxSizeExceeded(maxSize, attachmentTotalSize, oldAttachmentSize, newAttachmentFile.getLength())) {
				throw new VUserException(AttachmentMultilingualResources.MAX_TOTAL_SIZE_EXCEEDED, maxSize);
			}
			final FileInfoURI fileInfoUri = fileServices.saveAttachment(newAttachmentFile);
			attachment.setAttFiId((Long) fileInfoUri.getKey());
			attachment.setType(newAttachmentFile.getMimeType());
			attachment.setLength(newAttachmentFile.getLength());
			final Attachment savedAttachment = attachmentDAO.save(attachment);
			if (oldAttachmentFileId != null) {
				fileServices.deleteAttachment(oldAttachmentFileId);
			}
			return savedAttachment;
		}
		return attachmentDAO.save(attachment);
	}

	private static boolean isMaxSizeExceeded(final Long maxSize, final Long totalSize, final Long oldAttachmentSize, final Long newAttachmentSize) {
		return maxSize != null && maxSize > 0 && ((totalSize - oldAttachmentSize) + newAttachmentSize) > (1024 * 1024 * maxSize);
	}

	public DtList<Attachment> findAllByBotId(final long botId) {
		return attachmentDAO.findAll(Criterions.isEqualTo(DtDefinitions.AttachmentFields.botId, botId), DtListState.of(MAX_ELEMENTS_PLUS_ONE));
	}

	public void delete (final long attachmentId) {
		final Attachment attachment = findById(attachmentId);
		attachmentDAO.delete(attachmentId);
		fileServices.deleteAttachment(attachment.getAttFiId());
	}

	public DtList<AttachmentExport> exportAttachmentByBot(@SecuredOperation("botAdm") final Chatbot bot, final StringBuilder logs) {
		LogsUtils.addLogs(logs, "Export attachments : ");
		try {
			return findAllByBotId(bot.getBotId()).stream().map(attachment -> {
				attachment.attachmentFileInfo().load();
				final AttachmentFileInfo attachmentFileInfo = attachment.attachmentFileInfo().get();
				final AttachmentExport attachmentExport = new AttachmentExport();
				attachmentExport.setLabel(attachment.getLabel());
				attachmentExport.setFileName(attachmentFileInfo.getFileName());
				attachmentExport.setMimeType(attachmentFileInfo.getMimeType());
				attachmentExport.setLength(attachmentFileInfo.getLength());
				final VFile file = fileServices.getAttachment(attachment.getAttFiId());
				try (final InputStream inputStream = file.createInputStream()) {
					attachmentExport.setFileData(Base64.getEncoder().encodeToString(inputStream.readAllBytes()));
				} catch (final IOException e) {
					LogsUtils.logKO(logs);
					LogsUtils.addLogs(logs, e);
				}
				return attachmentExport;
			}).collect(VCollectors.toDtList(AttachmentExport.class));

		} catch (final Exception e) {
			LogsUtils.logKO(logs);
			LogsUtils.addLogs(logs, e);
			throw new VUserException(AttachmentMultilingualResources.EXPORT_UNEXPECTED_ERROR, e);
		}

	}

	@Override
	public void stop() {

	}
}
