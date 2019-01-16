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
        adjacentCells: [],
        currentPlayer: [],

        shipLocations: [],

        userShips: [],
        userSalvos: [],
        enemySalvos: [],

        ship: '',
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
                    app.shipLocation(app.userGrid);
                    app.shipLocation(app.opponentGrid);

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

        placeShips(ship) {

            fetch('/api/games/players/' + this.id + '/ships', {
                    credentials: 'include',
                    method: 'POST',
                    headers: {
                        'Accept': 'application/json',
                        'Content-Type': 'application/json'
                    },
                    body: JSON.stringify([{
                        type: ship,
                        location: ['A1', 'A2', 'A2']
                    }]),
                })
                .then(r => r.json().then(e => console.log(e)))
                .catch(e => console.log(e))
        },


        shipLocation(letter) {

            document.getElementById('userTable').addEventListener('click', (e) => {
                console.log(e.target.id)

                let location = e.target.id;
                app.adjacentCells = [];
                let gameId = e.target.id;
                let rowHeader = gameId.charAt(1);
                let columnHeader = gameId.charAt(2);

                let shipLocation = letter + rowHeader + columnHeader;
                let newNumber = parseInt(columnHeader);


                e.target.classList.add(this.ship);
                
                console.log(this.ship)


                //if(ship == 'carrier') {
                //   e.target.classList.add('carrier');
                // }

                /*$(e.target).click(function () {
                        $("#carrier").clone().appendTo("#" + location);
                    });*/


            });
        },
        
        selectTrump(){
            this.ship = 'carrier'
        },
        
        selectPutin(){
            this.ship = 'battleship'
        },
        
        selectMerkel(){
            this.ship = 'submarine'
        },
        
        selectMay(){
            this.ship = 'destroyer'
        },
        
        selectOrban(){
            this.ship = 'patrol'
        },
        
    },
})
