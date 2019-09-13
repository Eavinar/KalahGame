$(document).ready(function () {
    var stompClient = null;
    var user;

    function setConnected(connected) {
        $("#connect").prop("disabled", connected);
        $("#disconnect").prop("disabled", !connected);
    }

    function connect() {
        user = $('#name').val();
        var socket = new SockJS('/gs-guide-websocket');
        stompClient = Stomp.over(socket);

        stompClient.connect({'client-id': user}, function (frame) {
            setConnected(true);

            stompClient.subscribe('/topic/connection', function (message) {
                showMessage(message.body);
            });

            stompClient.subscribe('/topic/messages', function (data) {
                data = JSON.parse(data.body);
                if (user == data.user) {
                    if (data.status == "FAIL") {
                        window.location.replace("http://localhost:8080/");
                    }
                    alert(data.message);
                }
            });

            stompClient.subscribe('/topic/move', function (message) {
                updateBoard(message.body);
            });

            stompClient.subscribe('/topic/disconnect', function (message) {
                if (message.body.indexOf("disconnect") >= 0) {
                    alert(message.body);
                }
            });

            stompClient.send("/app/game", {}, JSON.stringify({'name': $("#name").val()}));
        });
    }

    function disconnect() {
        if (stompClient !== null) {
            stompClient.send("/app/disconnect", {}, user);
            stompClient.disconnect();
        }
        setConnected(false);
    }

    function showMessage(message) {
        $("#board").html(message);
    }

    function updateBoard(data) {
        data = JSON.parse(data);

        for (i = 0; i < data.pits.length; i++) {
            var boardId = i + 1;
            $('#gameBoard input#' + boardId).val(data.pits[i].stonesCount);
        }

        $('#gameBoard input#u1').val(data.stores[0].stonesCount);
        $('#gameBoard input#u2').val(data.stores[1].stonesCount);

        if (data.gameStatus == "USER1_WINS" || data.gameStatus == "USER2_WINS" || data.gameStatus == "DRAW") {
            alert(data.message);
            disconnect();
        }
    }

    $(function () {
        $("form").on('submit', function (e) {
            e.preventDefault();
        });
        $("#connect").click(function () {
            connect();
        });
        $("#disconnect").click(function () {
            disconnect();
        });
    });

    window.addEventListener("beforeunload", function (e) {
        disconnect();
    }, false);

    $("input.board").on('click', function () {
        stompClient.send("/app/updateBoard", {}, JSON.stringify({"user": user, "stepId": this.id}));
    });
});