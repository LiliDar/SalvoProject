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

        shipLocations: [],
        secondClickId: [],
        locationsArray: [],

        userShips: [],
        userSalvos: [],
        enemySalvos: [],

        shipType: {
            'carrier': 5,
            'battleship': 4,
            'submarine': 3,
            'destroyer': 3,
            'patrol': 2,
        },
        ship: '',
        shipClicks: 0,
        shipLogo: [],

        salvo: 2,
        salvoLocations: [],
        salvoType: {
            'shotOne': 1,
            'shotOne': 1,
            'shotOne': 1,
            'shotOne': 1,
            'shotOne': 1,
        },

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
        allValidLocations: [],

        select_box: true,
        post_box: false,
        salvo_box: false,
        opponent_table: true,
    },

    created: function () {
        this.fillAllValidLocationArray();
        this.getGamesData()
    },

    methods: {

        fillAllValidLocationArray() {
            let rows = this.rows;
            let columns = this.columns;
            let array = [];
            for (var i = 0; i < rows.length; i++) {
                for (var j = 0; j < columns.length; j++) {
                    array.push(rows[i] + columns[j]);
                }
            }
            this.allValidLocations = array;
        },

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
                    //app.printShips();
                    app.shipLocation();
                    app.salvoLocation();

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

        /*printShips() {
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
        },*/

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

            app.select_box = false;
            fetch('/api/games/players/' + this.id + '/ships', {
                    credentials: 'include',
                    method: 'POST',
                    headers: {
                        'Accept': 'application/json',
                        'Content-Type': 'application/json'
                    },
                    body: JSON.stringify(app.shipLocations),
                })
                .then(r => r.json().then(e => console.log(e)))
                .catch(e => console.log(e))

        },

        shipLocation() {


            app.select_box = true;

            document.getElementById('userTable').addEventListener('click', (e) => {


                let length = app.shipType[app.ship];

                let locationCell = e.target.id;
                let rowHeader = locationCell.charAt(1);
                let columnHeaderUntil9 = locationCell.charAt(2);
                let columnHeader;
                if (locationCell.charAt(3) != null) {
                    columnHeader = columnHeaderUntil9 + locationCell.charAt(3)
                } else {
                    columnHeader = columnHeaderUntil9
                }

                let isFinished = false;
                if (!e.target.classList.contains(app.ship)) {


                    app.post_box = false;
                    console.log("this is the first click")

                    let shipLocation = rowHeader + columnHeader;

                    let adjacentColumns = parseInt(columnHeader);
                    let adjacentRows = app.rows.indexOf(rowHeader);

                    let arrayRight = [];
                    let arrayLeft = [];
                    let arrayDown = [];
                    let arrayUp = [];


                    for (let i = 1; i < length; i++) {

                        if ((adjacentColumns + i) < 11) {
                            let cell = rowHeader + (adjacentColumns + i);
                            arrayRight.push(cell)

                            if (!app.allValidLocations.includes(cell))
                                arrayRight = []
                        }
                        if ((adjacentColumns - i) > -1) {
                            let cell = rowHeader + (adjacentColumns - i);
                            arrayLeft.push(cell)

                            if (!app.allValidLocations.includes(cell))
                                arrayLeft = []
                        }
                        if ((adjacentRows + i) < 11) {
                            let row = app.rows[adjacentRows + i];
                            let cell = row + adjacentColumns;
                            arrayDown.push(cell)

                            if (!app.allValidLocations.includes(cell))
                                arrayDown = []
                        }
                        if ((adjacentRows - i) > -1) {
                            let row = app.rows[adjacentRows - i];
                            let cell = row + adjacentColumns;
                            arrayUp.push(cell)

                            if (!app.allValidLocations.includes(cell))
                                arrayUp = []
                        }
                    }

                    if (arrayUp.length != length - 1) {
                        arrayUp = []
                    }
                    if (arrayDown.length != length - 1) {
                        arrayDown = []
                    }
                    if (arrayLeft.length != length - 1) {
                        arrayLeft = []
                    }
                    if (arrayRight.length != length - 1) {
                        arrayRight = []
                    }

                    arrayUp.forEach(cell => {
                        document.getElementById("U" + cell).classList.add("possible-cell")
                    })
                    arrayDown.forEach(cell => {
                        document.getElementById("U" + cell).classList.add("possible-cell")
                    })
                    arrayRight.forEach(cell => {
                        document.getElementById("U" + cell).classList.add("possible-cell")
                    })
                    arrayLeft.forEach(cell => {
                        document.getElementById("U" + cell).classList.add("possible-cell")
                    })

                    let arrayOfPossibleCellClass = document.getElementsByClassName("possible-cell");

                    let selectedArrayToPrintTheShips = [];
                    for (var i = 0; i < arrayOfPossibleCellClass.length; i++) {
                        let cell = arrayOfPossibleCellClass[i];
                        cell.addEventListener("click", (ev) => {
                            console.log("this is the second click")
                            app.select_box = true;

                            if (arrayUp.includes(ev.target.id.split("U")[1]))
                                selectedArrayToPrintTheShips = arrayUp;
                            if (arrayDown.includes(ev.target.id.split("U")[1]))
                                selectedArrayToPrintTheShips = arrayDown;
                            if (arrayLeft.includes(ev.target.id.split("U")[1]))
                                selectedArrayToPrintTheShips = arrayLeft;
                            if (arrayRight.includes(ev.target.id.split("U")[1]))
                                selectedArrayToPrintTheShips = arrayRight;


                            for (var j = 0; j < selectedArrayToPrintTheShips.length; j++) {
                                document.getElementById("U" + selectedArrayToPrintTheShips[j]).setAttribute("class", "userCell " + app.ship)
                                app.deleteElementOfAnArray(app.allValidLocations, selectedArrayToPrintTheShips[j])
                            }

                            app.deleteElementOfAnArray(app.allValidLocations, rowHeader + columnHeader)

                            arrayUp.forEach(cell => {
                                document.getElementById("U" + cell).classList.remove("possible-cell")
                            })
                            arrayDown.forEach(cell => {
                                document.getElementById("U" + cell).classList.remove("possible-cell")
                            })
                            arrayRight.forEach(cell => {
                                document.getElementById("U" + cell).classList.remove("possible-cell")
                            })
                            arrayLeft.forEach(cell => {
                                document.getElementById("U" + cell).classList.remove("possible-cell")
                            })


                            let shipToPush = {
                                type: app.ship,
                                locations: [...selectedArrayToPrintTheShips, rowHeader + columnHeader]
                            }

                            if (!app.shipLocations.includes(shipToPush) && shipToPush.locations.length == length)
                                app.shipLocations.push(shipToPush)

                            console.log(app.shipLocations)



                            arrayRight = [];
                            arrayLeft = [];
                            arrayDown = [];
                            arrayUp = [];
                            selectedArrayToPrintTheShips = [];

                            if (app.ship == 'patrol') {
                                app.post_box = true;
                                app.select_box = false;
                                app.opponent_table = true;
                            } else {
                                app.post_box = false;
                                app.select_box = true;
                                app.opponent_table = false;
                            }

                        })
                    }


                    e.target.classList.add(this.ship);

                }

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

        deleteElementOfAnArray(array, element) {

            var index = array.indexOf(element)
            array.splice(index, 1);
        },


        salvoLocation() {


            document.getElementById('opponentTable').addEventListener('click', (ev) => {


                    app.salvo_box = true;
                    let cell = ev.target;
                
                
                if(!cell.classList.contains("salvo")) {
                    cell.classList.add('salvo')
                } else {
                     cell.classList.remove('salvo')
                }
                let array = [];
                let otherArray = [];
                let allSalvos = document.getElementsByClassName("salvo");  
                for (var i = 0; i < allSalvos.length; i++) {
                    let salvo = allSalvos[i];
                    if(i < 5 ) {
                       array.push(salvo.id.split("O")[1]); 
                    } else {
                        otherArray.push(salvo);
                    }
                    
                }
                otherArray.forEach(el => el.classList.remove("salvo"))
                app.salvoLocations = array;

          

            });
    },

    placeSalvos() {


        fetch('/api/games/players/' + this.id + '/salvos', {
                credentials: 'include',
                method: 'POST',
                headers: {
                    'Accept': 'application/json',
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({locations: app.salvoLocations}),
            })
            .then(r => r.json().then(e => console.log(e)))
            .catch(e => console.log(e))
    },



},
})
