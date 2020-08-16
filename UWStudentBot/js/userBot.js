/**
 * @author Jonathan Ke
 * @email jak2@andrew.cmu.edu
 * @date 8/15/2020
 */
//static constants
const HEIGHT = 600;
const WIDTH = 600;
//user location relative to map
var x = 500;
var y = 500;
var dx = 0;
var dy = 0;
var userID;
var ct = 0;

var bgImg = new Image();
    bgImg.src = 'UWCampus.jpg';

function refresh(){
    var c = document.getElementById("myCanvas");
    var ctx = c.getContext("2d");
    ctx.drawImage(bgImg, x-WIDTH/2, y-HEIGHT/2, WIDTH, HEIGHT, 0, 0, 600, 600);
    ctx.arc(WIDTH/2+20, HEIGHT/2+20, 10, 0, 2 * Math.PI);
    ctx.fillStyle = "red";
    ctx.fill();
    // update coordinates
    var coords = document.getElementById("coordinates");
    coords.innerHTML = "(x,y): ("+x.toString()+", "+y.toString()+")";
    x += dx;
    y += dy;
}

function sendLocation(){
    var xhttp = new XMLHttpRequest();
    xhttp.open("GET", "https://dubhacks2020.wl.r.appspot.com/loc/"+x.toString()+"/"+y.toString()+"/"+userID.toString(), true);
    xhttp.send();
}

/** 
function getOtherUsers(){
    
    var myHttp = new XMLHttpRequest();
    if (!myHttp) {
        alert('Giving up :( Cannot create an XMLHTTP instance');
        return false;
    }
    myHttp.onreadystatechange = alertContents;

    myHttp.open("GET", "https://dubhacks2020.wl.r.appspot.com/info3", true);
    
    myHttp.send();
}
*/

/** 
function alertContents() {
    try {
      if (myHttp.readyState == XMLHttpRequest.DONE) {
        if (myHttp.status >= 200) {
          alert(myHttp.responseText);
        } else {
          alert('There was a problem with the request.');
        }
      }
    }
    catch( e ) {
      alert('Caught Exception: ' + e.description);
    }
}
*/

function runRepeater(){
    refresh();
    if (ct % 100 === 0){
        sendLocation();
        //getOtherUsers();
    }
    ct += 1
}


window.onload = function(){
    userID = Math.floor((Math.random() * 10000) + 1);
    setInterval(runRepeater, 10);
}

function move(dir) {
    if (dir == "up") {dy = -1; }
    if (dir == "down") {dy = 1; }
    if (dir == "left") {dx = -1; }
    if (dir == "right") {dx = 1; }
}

function clearmove(){
    dy = 0;
    dx = 0;
}
