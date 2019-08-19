var express = require("express");
var fs = require("fs");
var app = express();

app.get("/mobile_app/food_order_app/", (req, res, next) => {
    res.json(JSON.parse(fs.readFileSync("data.json")));
   });

app.listen(3000, () => {
 console.log("Server running on port 3000");
});