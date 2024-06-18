https://www.toptal.com/java/stomp-spring-boot-websocket

Kafka Commands

Zookeeper Command
 .\bin\windows\kafka-server-start.bat .\config\server.properties

Producer
 .\bin\windows\kafka-server-start.bat .\config\server.properties

Consumer
 .\bin\windows\kafka-console-consumer.bat --topic test --bootstrap-server localhost:9092 --from-beginning





 Updated JS
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


                 stompClient.subscribe("/topic/rideRequest",function(data){
                     console.log("new ride request"+data.body);
                     const bookingId = JSON.parse(data.body).bookingId;
                     const response = confirm(`You have a new ride request from passenger ${JSON.parse(data.body).passengerId}. Please provide your confirmation`);
                     if(response == true){
                         const id = prompt("Please enter your driverId");
                         stompClient.send(`/app/rideResponse/${id}`,{},JSON.stringify({response,bookingId}))
                     }
                     /*stompClient.send("/app/rideResponse",{},JSON.stringify({response}))
                     console.log(response);*/
                 })
             })



             /*stompClient.connect({},function(){
                 console.log("connected");
                 stompClient.subscribe("/topic/scheduled",function(message){
                     console.log("received a schedule message from server",message);
                     const li = document.createElement("li");
                     li.textContent = message.body;
                     document.getElementById("list").appendChild(li);
                 })
             })*/




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


