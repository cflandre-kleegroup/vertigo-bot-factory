Vue.component('v-chatbot-dev', {
		template : `
	<div class="bot">
		<q-scroll-area class="bg-grey-2 col-grow row q-pa-sm" ref="scroller">
			<div class="q-pr-md">
				<div v-for="(msg, index) in messages">
					<q-chat-message v-if="msg.rating" class= "animate-fade" :key="'msgRating-'+index" :sent="msg.sent" :bg-color= "msg.bgColor" :avatar = "msg.avatar" >
						<q-rating v-model="msg.rating" :max="5" style="font-size: 2rem;" readonly ></q-rating>
					</q-chat-message>
					<q-chat-message v-if="msg.text" class="animate-fade" :key="'msg-'+index" :label="msg.label" :text-sanitize="msg.sent" :sent="msg.sent" :text-color="msg.textColor"
						:bg-color="msg.bgColor" :name="msg.name" :avatar="msg.avatar" :text="msg.text" :stamp="msg.stamp" ></q-chat-message>
				</div>
				<div class="sys-chat">
					<q-chat-message v-if="error" class="animate-fade" bg-color="orange-4" text-color="black" size="12" >
						<div class="q-pb-sm">
							{{$q.lang.vui.chatbot.errorMessage}}
						</div>
						<q-btn class="full-width" @click="askBot(lastPayload)" :label="$q.lang.vui.chatbot.tryAgain" color="white" text-color="black" ></q-btn>
					</q-chat-message>
				</div>
				<div class="sys-chat non-selectable">
					<q-chat-message v-if="inputConfig.buttons.length > 0" class="animate-fade" bg-color="primary" size="12">
						<div class="text-blue-2 q-caption">
							{{$q.lang.vui.suggestedAnswers}}
						</div>
						<div class="row docs-btn">
							<q-btn v-for="(btn, index) in inputConfig.buttons" class="full-width" :key="'repChatBtn-'+index" @click="postAnswerBtn(btn)" :label="btn.title" color="white" text-color="black" ></q-btn>
						</div>
					</q-chat-message>
				</div>
				<div class="message-processing sys-chat non-selectable" >
					<q-chat-message v-if="processing" class="animate-fade" bg-color="grey-4">
						<q-spinner-dots size="2em"></q-spinner-dots>
					</q-chat-message>
				</div>
				<div class="non-selectable">
					<q-chat-message v-if="inputConfig.showRating" class="animate-fade" bg-color="primary" sent >
						<q-rating v-model="rating" :max="4" style="font-size: 2rem;" ></q-rating>
					</q-chat-message>
				</div>
			</div>
		</q-scroll-area>
		<div class="message-response row docs-btn q-pl-sm non-selectable">
			<q-input :type="inputConfig.modeTextarea ? 'textarea' : 'text'"
					 ref="input" dense
					 @keydown.enter.prevent="inputConfig.modeTextarea ? false : (inputConfig.responseText.trim() === '' && inputConfig.rating === 0) ? false : postAnswerText()"
					 :max-height="100"
					 class="col-grow"
					 v-model="inputConfig.responseText"
					 :placeholder="placeholder ? placeholder : $q.lang.vui.chatbot.inputPlaceHolder"
					 :disable="processing || error"
					 :loading="processing"></q-input>
		
			<q-btn round color="primary" icon="send" @click="postAnswerText()" :disable="processing || (inputConfig.responseText.trim() === '' && inputConfig.rating === 0)"></q-btn>
		</div>
		<div class="message-response row docs-btn q-pl-sm non-selectable justify-center" v-if="devMode === true">
			<q-btn round color="black" icon="arrow_back" @click="restart(false)"><q-tooltip>Back one step</q-tooltip></q-btn>
			<q-btn round color="red" icon="refresh" @click="restart(false)"><q-tooltip>Restart conversation</q-tooltip></q-btn>
		</div>
	</div>
	`
		,
		props : {
			botUrl : { type: String, required:true },
			devMode: { type: Boolean, 'default': false },
			minTimeBetweenMessages: { type: Number, 'default': 1000 },
			botAvatar: { type: String, required:true },
			botName: { type: String, required:true },
			placeholder: { type: String },
			startCall: {type:String, 'default' : '_start'}
		},
		data: function () {
			return {
				// config
				convId: 42,
				// technique
				inputConfig: {
					modeTextarea : false, // TODO, il exste d'autres modes, par ex email
					responseText: "",
					responsePattern : "",
					showRating : false,
					rating: 0,
					buttons: [],
				},
				prevInputConfig: {},
				lastPayload: null,
				processing: false,
				error: false,
				messages: [],
				keepAction: false,
				menu: false,
				lastUserInteraction: 0,
				watingMessagesStack: []
			}
		},
		created : function () {
			this.convId = Math.random();
			this.startConversation(this.startCall); // lancement de la phrase d'accueil
		},
		methods: {
			startConversation: function (){
				this.lastUserInteraction = Date.now();
				
				this.$http.post(this.startCall, {})
					.then(httpResponse => {}).catch(error => {
						// error
						this.error = true;
						
						this.processing = false;
						this._scrollToBottom();
					});
			},
			postAnswerBtn: function (btn) {
				this.messages.push({
					text: [btn.title],
					sent: true,
					bgColor: "primary",
					textColor: "white"
				});

				this._scrollToBottom();
				
				this.askBot(btn.payload);
			},
			postAnswerText: function () {
				var sanitizedString = this.inputConfig.responseText.trim().replace(/(?:\r\n|\r|\n)/g, '<br>');
				
				this.messages.push({
					text: sanitizedString !== '' ? [sanitizedString] : null,
					rating: this.inputConfig.rating > 0 ? this.inputConfig.rating : null,
					sent: true,
					bgColor: "primary",
					textColor: "white"
				});

				this._scrollToBottom();
				
				var response = this.inputConfig.responsePattern === "" ? sanitizedString.replace(/(")/g, "\"")
															  : this.inputConfig.responsePattern.replace("#", sanitizedString.replace(/(")/g, "\\\""));
				
				this.askBot(response);
			},
			_scrollToBottom: function () {
				if (this.$refs.scroller) {
					this.$refs.scroller.setScrollPosition(this.$refs.scroller.scrollSize, 400);
				}
			},
			askBot: function (value) {
				this.prevInputConfig = JSON.parse(JSON.stringify(this.inputConfig));
				this.reinitInput();
				this.lastPayload = value;
				this.processing = true;
				
				this.lastUserInteraction = Date.now();
				
				this.$http.post(this.botUrl, {sender: this.convId, message: value})
					.then(httpResponse => {
						// success
						httpResponse.data.forEach(function(value, key) {
							var textes = value.text.split(/<hr>|<hr \/>/);
							
							for (var i = 0; i < textes.length - 1; i++) {
								this.watingMessagesStack.push({text: textes[i]});
							}
							
							value.text = textes[textes.length - 1];
							this.watingMessagesStack.push(value);
						}, this);
						
						this._displayMessages();
					}).catch(error => {
						// error
						this.error = true;
						
						this.processing = false;
						this._scrollToBottom();
					});
					
			},
			_displayMessages: function () {
				if (this.watingMessagesStack.length > 0) {
					var currentMessage = this.watingMessagesStack.shift();
					var watingTime = this.lastUserInteraction - Date.now() + this.minTimeBetweenMessages;
					
					this.sleep(watingTime).then(() => {
						this._processResponse(currentMessage);
						this.lastUserInteraction = Date.now();
						this._displayMessages();
					});
				} else {
					this.processing = false;
					if (this.keepAction) {
						this.inputConfig = this.prevInputConfig;
						this.inputConfig.responseText = "";
						this.keepAction = false;
					}
					
					this.sleep(1).then(() => { // en différé le temps que la vue soit mise à jour
						this.$refs.input.focus();
					});
				}
			},
			_processResponse: function (response) {
			var lastMsg = this.messages[this.messages.length-1];
				if (lastMsg && !lastMsg.sent && !lastMsg.sys) {
					// ajoute un message à un précédent message du bot
					lastMsg.text.push(response.text);
				} else {
					// première réponse du bot
					this.messages.push({
						avatar: this.botAvatar,
						text: [response.text],
						bgColor: "grey-4"
					});
				}
				
				if (response.buttons) {
					response.buttons.forEach(function(value, key) {
						if (value.title.startsWith("#")) {
							var cmd = value.title.substring(1);
							if (cmd === "textarea") this.inputConfig.modeTextarea = true;
							if (cmd === "eval") this.inputConfig.showRating = true;
							if (cmd === "keep_action") this.keepAction = true;
							if (value.payload) this.inputConfig.responsePattern = value.payload;
						} else {
							this.inputConfig.buttons.push(value);
						}
					}, this);
				}
				
				this._scrollToBottom();
			},
			restart: function (silent) {
				if (!silent) {
					this.systemMessage(this.$q.lang.vui.chatbot.restartMessage);
				}
				
				this.$http.post(this.botUrl, '{"sender":"' + this.convId + '","message":"/restart"}')
				.then(httpResponse => {
					this.askBot("/start"); // lancement de la phrase d'accueil
				});
			},
			reinitInput: function () {
				this.inputConfig.modeTextarea = false;
				this.inputConfig.responsePattern = "";
				this.inputConfig.responseText = "";
				this.inputConfig.showRating = false;
				this.inputConfig.rating = 0;
				this.inputConfig.buttons = [];
				this.error = false;
			},
			sleep: function(milliseconds) {
			  return new Promise(function(resolve) {setTimeout(resolve, milliseconds)});
			},
			systemMessage: function(msg) {
				this.messages.push({
					text: [msg],
					bgColor: "orange",
					sys: true
				});
				this._scrollToBottom();
			}
		}
});