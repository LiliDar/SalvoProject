var app = new Vue({

    el: '#app',

    data: {
        allData: [],
        allInfo: [],
        show: false,
        appear: true,
        seen: true,
        gameData: [],
        currentPlayer: [],
        gamePlayer: [],
        user: {},
        isLogged: false,


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
                    app.modal();
                    app.tableModal();
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

                    swal("Success!", "You Logged In Successfully")
                    location.reload();
                    app.isLogged = true;
                    app.appear = false;
                    app.show = true
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

                    location.reload();
                    app.isLogged = false;
                    app.appear = true;
                    app.show = false;


                })
                .catch(e => console.log(e))
        },

        signUp() {

            let username = document.getElementById('username');
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

            location.reload();
            app.isLogged = true;
            app.appear = false;
            app.show = true
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

                    app.gameData = data.games;
                    app.currentPlayer = data.currentPlayer[0];
                    if (data.currentPlayer) {
                        app.isLogged = true;
                        app.appear = false;
                        app.show = true
                    }
                })
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

        createGame() {

            fetch("/api/games", {
                    credentials: 'include',
                    method: 'POST',
                    headers: {
                        'Accept': 'application/json',
                        'Content-Type': 'application/x-www-form-urlencoded'
                    },
                })
                .then(r => r.json())
                .then(r => location.replace('/web/game.html?gp=' + r.newGamePlayer))
                .catch(e => console.log(e))
        },

        joinGame(gameId) {
            fetch('/api/game/' + gameId + '/players', {
                    credentials: 'include',
                    method: 'POST',
                    headers: {
                        'Accept': 'application/json',
                        'Content-Type': 'application/x-www-form-urlencoded'
                    },
                })
                .then(r => r.json())
                .then(r => location.replace('/web/game.html?gp=' + r.gamePlayerID))
                .catch(e => console.log(e))
        },



        modal() {
            var openLoginRight = document.querySelector('.h1');
            var loginWrapper = document.querySelector('.login-wrapper');

            openLoginRight.addEventListener('click', function () {
                loginWrapper.classList.toggle('open');
            });
        },

        tableModal() {
            $(function () {

                var widthCont = 110;
                var slider = $('.slider');
                var login = $('.back');
                var signup = $('.join');
                $('.container').css('width', widthCont + 'rem');
                var sliderMargin = widthCont / 3.5;
                var sliderWidth = $('.slider').css('width').replace('px', '');
                slider.css('transform', 'translateX(-' + sliderWidth / 1.2 + 'px)');


                slider.css('margin-left', sliderMargin + "rem");

                $('.signUpForm').hide();

                login.slideUp();
                login.stop().click(function () {

                    $('.loginForm').fadeToggle(700);
                    $('.signUpForm').fadeToggle(700);
                    slider.css("transition", "all .3s cubic-bezier(.87,.32,.79,.6)");
                    slider.css('margin-left', sliderMargin + "rem");
                    slider.css("height", "34rem");
                    login.slideUp();
                    signup.slideDown();
                });
                signup.stop().click(function () {
                    $('.loginForm').fadeToggle(700);
                    $('.signUpForm').fadeToggle(700);
                    slider.css("transition", "all .5s cubic-bezier(.87,.32,.79,.6)");
                    slider.css('margin-left', sliderMargin * 3.15+ "rem");
                    signup.slideUp()
                    login.slideDown();
                    slider.css("height", "34rem");
                    $("input").css('margin', '2.5rem 0');
                });
             });
        },
    }
})
