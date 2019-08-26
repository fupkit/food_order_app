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
    var items = req.body;
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
    } catch(e) {
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