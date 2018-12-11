var app = new Vue({

    el: '#app',

    data: {
        id: location.search.split("=")[1],
        userShips: [],
        userSalvos: [],
        opponentSalvos: [],
        columnHeaders: ["", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10"],
        rowHeaders: ["A", "B", "C", "D", "E", "F", "G", "H", "I", "J"],
        gamePlayers: [],
        userInfo: [],
        playerEmail: [],
        opponentEmail: [],
        shipTable: "#tableContainerShip",
        salvoTable: "#tableContainerSalvo",
        shipGrid: "U",
        salvoGrid: "Y",
        placeShip: "#U",
        placeSalvo: "#Y",
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

                    app.userShips = data.userShips;
                    app.userSalvos = data.userSalvos;
                    app.gamePlayers = data.game.gamePlayer;
                    app.userInfo = data.userInfo.player;
                    
                    app.createTable(app.shipTable, app.shipGrid);
                    app.createTable(app.salvoTable, app.salvoGrid);
                    app.printShips(app.userShips, app.placeShip);
                    app.printShips(app.userSalvos, app.placeSalvo);
                    app.getPlayers();
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

        createTable(table,grid) {
            function generateGrid(tableId, columnHeaders, rowHeaders) {

                let rows = rowHeaders.length;
                let cols = columnHeaders.length;
                var grid = "<table>";
                for (row = 0; row <= rows; row++) {
                    grid += "<tr>";
                    var indexRow = $("tr").index(this) + row + 2;
                    for (col = 0; col < cols; col++) {
                        var indexColumn = $("td").index(this) + col + 2;
                        if (indexRow == 1) {
                            grid += "<td class='myColumnHeaders'>" + columnHeaders[col] + "</td>";
                        } else if (indexColumn == 1) {
                            grid += "<td class='myRowHeaders'>" + rowHeaders[row - 1] + "</td>"
                        } else {
                            grid += "<td class='emptyCells' id=" + tableId + rowHeaders[row - 1] + columnHeaders[col] + ">" + rowHeaders[row - 1] + columnHeaders[col] + "</td>";
                        }
                    }
                    grid += "</tr>";

                }
                return grid;
            }
            $(table).append(generateGrid(grid, app.columnHeaders, app.rowHeaders));
        },

        printShips(ships, grid) {
            for (var i = 0; i < ships.length; i++) {
                let shipLocations = ships[i].locations;
                let shipType = ships[i].type;
                for (var j = 0; j < shipLocations.length; j++) {
                    let location = shipLocations[j];
                    if (location != null) {
                        $(grid + location).addClass('ship-location ' + shipType);
                    }
                }
            }
        },
    },
})
