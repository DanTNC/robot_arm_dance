$(function(){
    var socket = io();
    socket.on('connect', function() {
        socket.emit('connected', {data: 'I\'m connected!'});
    });

    var refresh = () => {
      $.ajax({
        url: "/probe/states",
        success: (res) => {
            console.log(res);
            $("#states").text(JSON.stringify(res));
        }
      });
      $.ajax({
        url: "/probe/actions",
        success: (res) => {
            console.log(res);
            var text = "";
            for(let action of res){
                text += JSON.stringify(action) + "<br>"
            }
            $("#actions").html(text);
        }
      })
      $.ajax({
        url: "/probe/choreography",
        success: (res) => {
            console.log(res);
            var text = "";
            for(let choreography of res){
                text += JSON.stringify(choreography) + "<br>"
            }
            $("#choreography").html(text);
        }
      })
    };
    
    refresh();

    socket.on('refresh', function() {
        refresh();
    });

    $("#refresh").click(() => {
        refresh();
    });
});