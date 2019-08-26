var express = require("express");
var fs = require("fs");
var app = express();
app.use(express.json());

var menu = JSON.parse(fs.readFileSync(__dirname + "/data.json"));
var orderId = 1;
app.get("/mobile_app/food_order_app/get_menu", (req, res) => {
    res.json(menu);
});

app.post("/mobile_app/food_order_app/post_order", (req, res) => {
    console.log(JSON.stringify(req.body));
    var items = req.body.order.orderItems;
    var success = false;
    try {
        items.forEach(item => {
            menu.items.forEach(menuItem => {
                if (menuItem.itemId === item.itemId) {
                    if (parseInt(menuItem.remain) > parseInt(item.quantity)) {
                        menuItem.remain -= item.quantity;
                        success = true;
                    }
                    console.log("Item " + menuItem.itemId + " remain " + menuItem.remain);
                }
            })
        });
    } catch (e) {
        console.log(e.message);
    }
    if (success) {
        res.send({ "orderId": orderId++ });
    } else {
        res.send({ "orderId": -1 });
    }
});
app.use('/images', express.static(__dirname + '/images'));
app.listen(3000, () => {
    console.log("Server running on port 3000");
});


//firebase
var admin = require("firebase-admin");

var serviceAccount = require("./food-order-app-firebase-key.json");

admin.initializeApp({
    credential: admin.credential.cert(serviceAccount),
    databaseURL: ""
});
app.post("/mobile_app/food_order_app/send_nf", (req, res) => {
    console.log(JSON.stringify(req.body));
    var body = req.body;
    var success = false;
    var token = body.fcmToken;
    var orderId = body.orderId;
    var payload = {
        data: {
            orderId : orderId.toString(),
            title: "VTC Canteen",
            body: "Order " + orderId + " Finished! Please take your meal."
        }
    };

    var options = {
        priority: "high",
        timeToLive: 60 * 60 * 24
    };
    admin.messaging().sendToDevice(token, payload, options)
        .then(function (response) {
            console.log("Successfully sent message:", response);
            res.send({ "orderId": orderId });
        })
        .catch(function (error) {
            console.log("Error sending message:", error);
            res.send({ "orderId": -1 });
        });
    

});