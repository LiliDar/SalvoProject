var app = new Vue({

    el: '#app',

    data: {
        allGames: [],
        creationDate: {},
        gamePlayer: {},
        gameID: {},


    },

    created: function () {
        this.getGamesData()
    },


    methods: {

        getGamesData() {

            fetch("/api/games")
                .then(function (response) {
                    return response.json()
                })
                .then(function (json) {
                    console.log(json);
                    data = json;

                    /*app.renderList(data);*/
                    app.allGames = data;

                    /*app.getCorrectDate();*/
                    app.getGameData();
                   
                })
        },

        getGameData() {

            for (i = 0; i < app.allGames.length; i++) {
                
                app.creationDate = new Date(app.allGames[i].creationDate).toLocaleString();
                
                
                app.gameID = app.allGames[i].id;
               
                
                app.gamePlayer = app.allGames[i].gamePlayers;
                
            }
        },

      








        /*getCorrectDate(){
            var date = [];
            var player = [];
            var id = [];
            for(var i = 0; i < this.allGames.length; i++) {
                var correctDate = this.allGames[i].creationDate;
                date.push(new Date(correctDate).toLocaleString());
                
                var gamePlayers = this.allGames[i].gamePlayers;
                player.push(gamePlayers)
                
                var gameID = this.allGames[i].id;
                id.push(gameID)
            }
            this.creationDate = date;
            console.log(this.creationDate)
            
            this.gamePlayer = player;
            console.log(this.gamePlayer)
            
            this.gameID = id;
            console.log(this.gameID)
        },*/







        /*getListHtml(data) {
            return data.map(getItemHtml => getItemHtml.creationDate);
        },

        renderList(data) {
            this.getListHtml(data).forEach(e => {

                var li = document.createElement("li");
                li.innerHTML = e
                document.getElementById("list").append(li)
            });
        },*/
    }
})
