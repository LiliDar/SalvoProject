var app = new Vue({

    el: '#app',

    data: {
        allData: [],
        allInfo: [],
    },

    created: function () {
        this.getGamesData()
    },


    methods: {

        getGamesData() {

            fetch("/api/leader-board")
                .then(function (response) {
                    return response.json()
                })
                .then(function (json) {
                    console.log(json);
                    data = json;

                    app.allData = data;

                    app.getData();
                    app.logIn();
                   


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

            fetch("/api/login", {
                    credentials: 'include',
                    method: 'POST',
                    headers: {
                        'Accept': 'application/json',
                        'Content-Type': 'application/x-www-form-urlencoded'
                    },
                    body: 'email=j.bauer@ctu.gov&password=123',
                })
                .then(r => {
                    if (r.status == 200)
                        console.log(r)
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
                })
                .catch(e => console.log(e))
        }
    }
})
