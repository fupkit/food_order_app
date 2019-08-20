var express = require("express");
var fs = require("fs");
var app = express();

app.get("/mobile_app/food_order_app/get_menu", (req, res, next) => {
    res.json(JSON.parse(fs.readFileSync("data.json")));
});

app.use('/images', express.static(__dirname + '/images'));
app.listen(3000, () => {
 console.log("Server running on port 3000");
});