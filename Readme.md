
//https://www.toptal.com/java/stomp-spring-boot-websocket

**Client side webocket code for browser


```
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <script src="https://cdn.jsdelivr.net/npm/sockjs-client@1/dist/sockjs.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/stompjs@2.3.3/lib/stomp.min.js"></script>
    <title>Document</title>
</head>
<body>
    <div>
        <!--<button onclick="connect()">Connect</button>-->
    </div>

    <div>
        <input type="text" id="name" placeholder="name">
        <input type="text" id="message" placeholder="message">
        <button onclick="submit()">Submit</button>
    </div>

    <ul id = "list">

    </ul>

    <script>
        document.addEventListener("DOMContentLoaded",()=>{
            connect();
        })
        var stompClient = null;
        function connect(){
            const socket = new SockJS("http://localhost:8080/ws")
            stompClient = Stomp.over(socket);
            stompClient.connect({},function(){
                console.log("connected");
                stompClient.subscribe("/topic/ping",function(message){
                    console.log("received a new message from server",message);
                })

                stompClient.subscribe("/topic/message",function(data){
                const message = JSON.parse(data.body);
                console.log("-----------------",message);
                const li = document.createElement("li");
                li.textContent = message.name + " -- " + message.message + " -- " + message.timeStamp;
                document.getElementById("list").appendChild(li);
                })
            })

           

            stompClient.connect({},function(){
                console.log("connected");
                stompClient.subscribe("/topic/scheduled",function(message){
                    console.log("received a schedule message from server",message);
                    const li = document.createElement("li");
                    li.textContent = message.body;
                    document.getElementById("list").appendChild(li);
                })
            })

           
        }

        function submit(){
            const name = document.getElementById("name").value;
            const message = document.getElementById("message").value;
            console.log(name+"---------------"+message);
            stompClient.send("/app/chat",{},JSON.stringify({name,message}));
        }
    </script>
</body>
</html>
```
