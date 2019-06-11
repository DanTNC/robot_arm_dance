$(function(){
    var socket = io('http://' + document.domain + ':' + location.port);
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
            $("#actions").text(res);
        }
      })
      $.ajax({
        url: "/probe/choreography",
        success: (res) => {
            console.log(res);
            $("#choreography").text(res);
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