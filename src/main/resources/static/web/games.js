var app = new Vue({

    el: '#app',

    data: {
        allData: [],
        allInfo: [],
        show: false,
        appear: true,
        gameData: [],
        gamePlayer: [],
        info: [],
        user: {},
        form: true,
        leaderTable: false,


    },

    created: function () {
        this.getGamesData()
        this.getUser();
    },


    methods: {

        getGamesData() {

            fetch("/api/leader-board")
                .then(function (response) {
                    return response.json()
                })
                .then(function (json) {
                    data = json;

                    app.allData = data;
                    console.log(app.allData)
                    app.getData();
                    app.findGames();
                    app.modal()

                })
        },

        getData() {
            let container_one = [];
            for (let i = 0; i < app.allData.length; i++) {
                container_one.push(app.allData[i])
            }
            this.allInfo = container_one;
        },

        logIn() {


            let username = document.getElementById('username');
            let password = document.getElementById('password');

            fetch("/api/login", {
                    credentials: 'include',
                    method: 'POST',
                    headers: {
                        'Accept': 'application/json',
                        'Content-Type': 'application/x-www-form-urlencoded'
                    },

                    body: 'email=' + username.value + '&password=' + password.value,

                })
                .then(r => {
                    if (r.status == 200)
                        console.log(r)

                    app.appear = false;
                    app.show = true;
                    app.form = false;
                    app.leaderTable = true;
                })
                .catch(e => console.log(e))
        },

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
                    app.appear = true;
                    app.form = true;
                    app.leaderTable = false;

                })
                .catch(e => console.log(e))
        },

        signUp() {

            let username = document.getElementById('userName');
            let password = document.getElementById('password');

            fetch("/api/players", {
                    credentials: 'include',
                    method: 'POST',
                    headers: {
                        'Accept': 'application/json',
                        'Content-Type': 'application/x-www-form-urlencoded'
                    },
                    body: 'email=' + username.value + '&password=' + password.value,
                })
                .then(r => r.json().then(e => console.log(e)))
                .catch(e => console.log(e))
        },

        findGames() {
            fetch("/api/games", {
                    credentials: 'include',
                    method: 'GET',
                    headers: {
                        'Accept': 'application/json',
                        'Content-Type': 'application/x-www-form-urlencoded'
                    },
                })
                .then(function (response) {
                    return response.json()
                })
                .then(function (json) {
                    console.log(json)
                    data = json;

                    app.gameData = data;
                    app.getGame();
                })
        },


        getGame() {
            let game = app.gameData;

            for (let i = 0; i < game.length; i++) {
                this.gamePlayer.push(game[i].gamePlayer)
            }
            for (let i = 0; i < this.gamePlayer.length; i++) {

            }

        },

        getUser() {
            fetch("/api/players", {
                    credentials: 'include',
                    method: 'GET',
                    headers: {
                        'Accept': 'application/json',
                        'Content-Type': 'application/x-www-form-urlencoded'
                    },
                })
                .then(r => r.json().then(e => app.user = e.succed[0]))
                .catch(e => console.log(e))
        },

        modal() {
            var openLoginRight = document.querySelector('.h1');
            var loginWrapper = document.querySelector('.login-wrapper');

            openLoginRight.addEventListener('click', function () {
                loginWrapper.classList.toggle('open');
            });
        },







    }
})
