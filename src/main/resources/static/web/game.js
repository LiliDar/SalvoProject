var app = new Vue({

    el: '#app',

    data: {
        id: location.search.split("=")[1],
        columns: ["1", "2", "3", "4", "5", "6", "7", "8", "9", "10"],
        rows: ["A", "B", "C", "D", "E", "F", "G", "H", "I", "J"],

        mainData: [],
        gamePlayers: [],
        userInfo: [],
        playerEmail: [],
        opponentEmail: [],
        currentPlayer: [],

        adjacentCells: [],
        shipLocations: [],
        secondClickId: [],
        locationsArray: [],

        userShips: [],
        userSalvos: [],
        enemySalvos: [],

        shipType: {
            'carrier': 5,
            'battelship': 4,
            'submarine': 3,
            'destroyer': 3,
            'patrol': 2,
        },
        ship: '',
        shipClicks: 0,
        shipLogo: [],

        userGrid: "U",
        opponentGrid: "O",

        placeUserSalvo: "#U",
        placeOpponentSalvo: "#O",

        userTables: "userTable",
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
                    app.userInfo = data.userInfo;
                    app.getPlayers();
                    app.currentPlayer = data.currentPlayer

                    app.userShips = data.userShips;
                    app.userSalvos = data.userSalvos;
                    app.enemySalvos = data.enemySalvos;


                    /*app.printSalvos(app.userSalvos, app.placeOpponentSalvo);
                    app.printSalvos(app.enemySalvos, app.placeUserSalvo);*/
                    app.printShips();

                    

                    app.shipLocation()

                })

        },

        getPlayers() {
            let playerInfo = [];
            let idInfo = [];
            let player = [];
            let opponent = [];

            let gamePlayer = this.gamePlayers;
            let user = this.userInfo;

            for (i = 0; i < gamePlayer.length; i++) {
                playerInfo.push(gamePlayer[i].email)
                idInfo.push(gamePlayer[i].playerId);

                if (idInfo == user.playerId) {
                    player.push(user.email)

                } else {
                    opponent.push(playerInfo[1]);
                }
            }
            this.playerEmail = player;
            this.opponentEmail = opponent;
        },

        printShips() {
            for (var i = 0; i < app.userShips.length; i++) {
                let shipLocations = app.userShips[i].locations;
                let shipType = app.userShips[i].type;
                for (var j = 0; j < shipLocations.length; j++) {
                    let location = shipLocations[j];
                    if (location != null) {
                        $("button").click(function () {
                            $("#carrier").clone().appendTo("#U" + location);
                        });
                    }
                }
            }
        },

        /*printSalvos(salvo, grid) {
            for (var i = 0; i < salvo.length; i++) {
                let salvoLocations = salvo[i].locations;
                for (var j = 0; j < salvoLocations.length; j++) {
                    let location = salvoLocations[j];
                    if (location != null) {
                        $(grid + location).addClass('salvo-location');
                    }
                }
            }
        },*/

        logOut() {

            fetch("/api/logout", {
                    credentials: 'include',
                    method: 'POST',
                    headers: {
                        'Accept': 'application/json',
                        'Content-Type': 'application/x-www-form-urlencoded'
                    },
                })
                .then(r => {
                    if (r.status == 200)
                        console.log(r)

                    app.show = false;
                })
                .catch(e => console.log(e))
        },

        placeShips() {

            fetch('/api/games/players/' + this.id + '/ships', {
                    credentials: 'include',
                    method: 'POST',
                    headers: {
                        'Accept': 'application/json',
                        'Content-Type': 'application/json'
                    },
                    body: JSON.stringify([{
                        type: app.ship,
                        location: app.shipLocations
                    }]),
                })
                .then(r => r.json().then(e => console.log(e)))
                .catch(e => console.log(e))
        },

        letters(letter) {
            if (letter == 'A') {
                return letter = 1;
            } else if (letter == 'B') {
                return letter = 2;
            } else if (letter == 'C') {
                return letter = 3;
            } else if (letter == 'D') {
                return letter = 4;
            } else if (letter == 'E') {
                return letter = 5;
            } else if (letter == 'F') {
                return letter = 6;
            } else if (letter == 'G') {
                return letter = 7;
            } else if (letter == 'H') {
                return letter = 8;
            } else if (letter == 'I') {
                return letter = 9;
            } else if (letter == 'J') {
                return letter = 10;
            } else {
                console.log('error')
            }
        },

        numbers(number) {
            app.letters(app.rows)
            if (number == 1) {
                return number = 'A';
            } else if (number == 2) {
                return number = 'B';
            } else if (number == 3) {
                return number = 'C';
            } else if (number == 4) {
                return number = 'D';
            } else if (number == 5) {
                return number = 'E';
            } else if (number == 6) {
                return number = 'F';
            } else if (number == 7) {
                return number = 'G';
            } else if (number == 8) {
                return number = 'H';
            } else if (number == 9) {
                return number = 'I';
            } else if (number == 10) {
                return number = 'J';
            } else {
                console.log('error')
            }
        },


        shipLocation() {

            document.getElementById('userTable').addEventListener('click', (e) => {


                let locationCell = e.target.id;
                console.log(locationCell)
                let rowHeader = locationCell.charAt(1);
                let columnHeader = locationCell.charAt(2);
                let adjacentRows = app.numbers(app.rows);
                console.log(adjacentRows)

                let shipLocation = "U" + rowHeader + columnHeader;
                app.shipLocations.push(shipLocation);
                console.log(app.shipLocations.toString())

                let adjacentColumns = parseInt(columnHeader);
                let arrayRight = [];
                let arrayLeft = [];
                let arrayDown = [];
                let arrayUp = [];

                for (let i = 1; i < 5; i++) {
                    if ((adjacentColumns + i) < 11) {
                        let cell = rowHeader + (adjacentColumns + i);
                        arrayRight.push(cell)
                    }
                    if ((adjacentColumns - i) > 0) {
                        let cell = rowHeader + (adjacentColumns - i);
                        arrayLeft.push(cell)
                    }
                    if ((adjacentRows + i) < 11) {
                        let row = letter(adjacentRows + i);
                        let cell = row + adjacentColumns;
                        arrayDown.push(cell)
                    }
                    if ((adjacentRows - i) > 0) {
                        let row = letter(adjacentRows - i);
                        let cell = row + adjacentColumns;
                        arrayUp.push(cell)
                    }
                }
                console.log(arrayUp, arrayDown)

                if (arrayRight.length <= 3) {
                    arrayRight.forEach(cell => {
                        document.getElementById("U" + cell).classList.add("not-possible-cell")
                    })

                } else {
                    arrayRight.forEach(cell => {
                        document.getElementById("U" + cell).classList.add("possible-cell")
                    })
                }


                if (arrayLeft.length <= 3) {
                    arrayLeft.forEach(cell => {
                        document.getElementById("U" + cell).classList.add("not-possible-cell")
                    })
                } else {
                    arrayLeft.forEach(cell => {
                        document.getElementById("U" + cell).classList.add("possible-cell")
                    })
                }

                e.target.classList.add(this.ship);


            });
        },

        selectTrump() {
            this.ship = 'carrier'
        },

        selectPutin() {
            this.ship = 'battleship'
        },

        selectMerkel() {
            this.ship = 'submarine'
        },

        selectMay() {
            this.ship = 'destroyer'
        },

        selectOrban() {
            this.ship = 'patrol'
        },




    },
})
