//jshint esnext:true
//jshint loopfunc:true

var getUrl = window.location.toString().replace("/chats", " ");
var baseUrl = getUrl.split(" ")[0];

var sockJS = new SockJS(baseUrl + "/ws");
var stompClient = Stomp.over(sockJS);
var my_id = 0;
var actual_chat_id = 0;

var opened = [];

function onMessageReceived(message) {
	
	if(message.fromId != my_id){
		var main_cont = document.createElement('div');
		main_cont.className = "row w-100";
		
		var text_cont = document.createElement('div');
		text_cont.className = "message-received";
		var textnode = document.createTextNode(message.message);
		
		text_cont.appendChild(textnode);
		main_cont.appendChild(text_cont);
		
		var chat_content_container = document.getElementById("chat_content_container");
		
		chat_content_container.appendChild(main_cont);
		chat_content_container.scrollTop = chat_content_container.scrollHeight;
	}
	
}

function onMessageSent(message) {
	
	var main_cont = document.createElement('div');
	main_cont.className = "row w-100";
	
	var text_cont = document.createElement('div');
	text_cont.className = "message-sent";
	var textnode = document.createTextNode(message.message);
	
	text_cont.appendChild(textnode);
	main_cont.appendChild(text_cont);
	
	var chat_content_container = document.getElementById("chat_content_container");
	
	chat_content_container.appendChild(main_cont);
	chat_content_container.scrollTop = chat_content_container.scrollHeight;
}

function onOpenChat(chatId) {
	
	if (!opened.includes(chatId)){
	
		stompClient.subscribe(
			      "/topic/messages/" + chatId,
			      (msg) => onMessageReceived(JSON.parse(msg.body))
			    );
		
		opened.push(chatId);
	}
	
}

function addMessages(messages) {
	
	for (let message of messages) {
		
		if(my_id == message.fromId) {
			onMessageSent(message);
		} else {
			onMessageReceived(message);
		}
		
	}
	
}

function loadChat(chat_id, username) {
	
	actual_chat_id = chat_id;
	
	var username_header = document.getElementById("active_username");
	username_header.innerHTML = "";
	
	var username_h5 = document.createElement('h5');
	var textnode = document.createTextNode(username);
	username_h5.appendChild(textnode);
	username_header.appendChild(username_h5);
	
	var xhr = new XMLHttpRequest();
	xhr.open("GET", baseUrl + "/api/chat/" + actual_chat_id +"/messages", true);
	xhr.onload = function (e) {
	  if (xhr.readyState === 4) {
	    if (xhr.status === 200) {
	      
	    	document.getElementById("chat_content_container").innerHTML = "";
	    	
	    	var messages = JSON.parse(xhr.response);
	    	
		    addMessages(messages);

		    onOpenChat(actual_chat_id);
		    
	    } else {
	      console.error(xhr.statusText);
	    }
	  }
	};
	xhr.onerror = function (e) {
	  console.error(xhr.statusText);
	};
	xhr.send(null);
}

function addChatInfo(chat_list) {
	
	var urlParams = new URLSearchParams(window.location.search);
	var init_chat =  urlParams.get('chat_Id');

	for (let chat of chat_list) {
		
		var main_div = document.createElement('div');
		main_div.className = "row chat_seller";
		
		var img_div = document.createElement('div');
		img_div.className = "col seller_img_container";
		
		var img = document.createElement('img');
		img.className = "seller_img";
		img.src = "/resources/images/user_icon.png";
		img_div.appendChild(img);
		
		var username_div = document.createElement('div');
		username_div.className = "col-md-7 seller_name_container";
		
		var username_h5 = document.createElement('h5');
		var textnode = document.createTextNode(chat.seller.userName);
		username_h5.appendChild(textnode);
		username_div.appendChild(username_h5);
		
		var btn_div = document.createElement('div');
		btn_div.className = "col-md-2 go_btn_container";
		
		var btn = document.createElement('a');
		btn.className = "btn btn-primary btn-nav btn-chat";
		btn.id = "btn_chat_" + chat.chatId;
		btn.onclick = function () { loadChat(chat.chatId, chat.seller.userName); };
		btn.appendChild(document.createTextNode(">"));
		btn_div.appendChild(btn);
		
		main_div.appendChild(img_div);
		main_div.appendChild(username_div);
		main_div.appendChild(btn_div);
		
		document.getElementById("chat_inbox").appendChild(main_div);
		
		if (init_chat !== null || init_chat !== ""){
			
			if (init_chat == chat.chatId){
				loadChat(chat.chatId, chat.seller.userName);
			}
		}
		
	}
}

function loadChats() {
	
	var xhr = new XMLHttpRequest();
	xhr.open("GET", baseUrl + "/api/chat/" + my_id +"/chats", true);
	
	xhr.onload = function (e) {
	  if (xhr.readyState === 4) {
	    if (xhr.status === 200) {
	    	
	      var chat_list = JSON.parse(xhr.response);
	      
	      addChatInfo(chat_list);
	      
	    } else {
	      console.error(xhr.statusText);
	    }
	  }
	};
	
	xhr.onerror = function (e) {
	  console.error(xhr.statusText);
	};
	
	xhr.send(null);
	
	
}

function buildSendOnClick(){
	
	document.getElementById("btn-chat-input-send").onclick = function () { 
			var message = document.getElementById("chat-send-input").value;
		
			if (message !== null && message !== "") {
				var message_obj = {
				    	fromId: my_id,
				        message: message,
				        date: Date.now(),
				    };
				
					if(actual_chat_id > 0){
						stompClient.send("/app/chat/" + actual_chat_id, {}, JSON.stringify(message_obj));
					}
					
					document.getElementById("chat-send-input").value = "";
					
					onMessageSent(message_obj);
			}
			
		};
	
}

window.onload = function () {
	my_id = document.getElementsByClassName("main-chat")[0].id.substring(10);
	
	stompClient.connect({}, function (frame) {

        console.log("connected to: " + frame);
        
        loadChats();
    	
    	buildSendOnClick();

    });
};