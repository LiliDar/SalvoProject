var app = new Vue({

    el: '#app',

    data: {
        id: location.search.split("=")[1],
        columnHeaders: ["","1", "2", "3", "4", "5", "6", "7", "8", "9", "10"],
        rowHeaders: ["A", "B", "C", "D", "E", "F", "G", "H", "I", "J"],
        
        gamePlayers: [],
        userInfo: [],
        playerEmail: [],
        opponentEmail: [],
        
        userShips: [],
        userSalvos: [],
        enemySalvos: [],
        
        userGrid: "U",
        opponentGrid: "Y",
        
        placeUserSalvo: "#U",
        placeOpponentSalvo: "#Y",
        
        userTables:"userTable",
        userHeader: "userTable-headers",
        userRow: "userTable-rows",
        opponentTable: "oponentTable",
        opponentHeader: "oponentTable-headers",
        opponentRow: "oponentTable-rows",
    },

    created: function () {
        this.getGamesData()
    },

    methods: {

        getGamesData() {

            fetch("/api/game_view/" + this.id)
                .then(function (response) {
                    return response.json()
                })
                .then(function (json) {
                    console.log(json);
                    data = json;

                    
                    app.gamePlayers = data.game.gamePlayer;
                    app.userInfo = data.userInfo.player;
                    app.getPlayers();
                
                    app.userShips = data.userShips;
                    app.userSalvos = data.userSalvos;
                    app.enemySalvos = data.enemySalvos;
                
                    app.gameTable(app.userTables, app.userHeader, app.userRow, app.userGrid);
                    app.gameTable(app.opponentTables, app.opponentHeader, app.opponentRow, app.opponentGrid);
                    
                    app.printShips();
                    app.printSalvos(app.userSalvos, app.placeOpponentSalvo);
                    app.printSalvos(app.enemySalvos, app.placeUserSalvo);

                })
        },

        getPlayers() {
            let playerInf = [];
            let player = [];
            let opponent = [];

            let gamePlayer = this.gamePlayers;
            let user = this.userInfo;

            for (i = 0; i < gamePlayer.length; i++) {
                let players = gamePlayer[i].player;
                playerInf.push(players);
            }

            for (i = 0; i < playerInf.length; i++) {
                if (playerInf[i].id === this.userInfo.id) {
                    player.push(this.userInfo.email)

                } else {
                    opponent.push(playerInf[i].email);
                }
            }
            this.playerEmail = player;
            this.opponentEmail = opponent;
        },

        gameTable(table, header, row, grid) {
            
            var rowHead = this.rowHeaders;
            var columnHead = this.columnHeaders;

            var table = document.getElementById(table);
            var theader = document.getElementById(header);
            var tbody = document.getElementById(row);

            var tRow0 = document.createElement("tr");
            var td0 = document.createElement("td");
            tRow0.appendChild(td0);

            for (var i = 0; i < 10; i++) {
                var tdNumbers = document.createElement("td");
                tdNumbers.innerHTML = i + 1;

                tRow0.appendChild(tdNumbers);
            }
            theader.appendChild(tRow0);

            for (var j = 0; j < 10; j++) {
                var rows = document.createElement("tr");
                var tdLetters = document.createElement("td");
                tdLetters.innerHTML = String.fromCharCode(65 + j);

                rows.append(tdLetters);
                tbody.append(rows);

                for (var k = 0; k < 10; k++) {
                    var tdBoard = document.createElement("td");
                    tdBoard.id = grid + rowHead[j] + columnHead[k];

                    rows.append(tdBoard);
                }
            }
        },

        printShips() {
            for (var i = 0; i < app.userShips.length; i++) {
                let shipLocations = app.userShips[i].locations;
                let shipType = app.userShips[i].type;
                for (var j = 0; j < shipLocations.length; j++) {
                    let location = shipLocations[j];
                    if (location != null) {
                        $("#U" + location).addClass('ship-location ' + shipType);
                    }
                }
            }
        },
        
        printSalvos(salvo,grid) {
            for (var i = 0; i < salvo.length; i++) {
                let salvoLocations = salvo[i].locations;
                console.log(salvoLocations)
                for (var j = 0; j < salvoLocations.length; j++) {
                    let location = salvoLocations[j];
                    if (location != null) {
                        $(grid + location).addClass('salvo-location');
                    }
                }
            }
        },
    },
})
