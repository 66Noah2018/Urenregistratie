/* 
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/JavaScript.js to edit this template
 */


function servletRequestPost(url, body) {
    const http = new XMLHttpRequest();
    let response = null;
    http.open("POST", url, false);
    http.setRequestHeader("Content-Type", "application/json;charset=UTF-8");
    
    http.onload = function(){ 
        while (http.readyState !== 4) {}
        if (http.readyState === 4) {
            response = http.responseText;
        }
    };
    http.send(JSON.stringify(body));
    while (response === null) {}
    return response;
}

function servletRequest(url){
    const http = new XMLHttpRequest();
    http.open("GET", url, false);
    let response = null;
    http.onload = function() {
//        while (http.readyState !== 4) {}
        if (http.readyState === 4 && http.status === 200) {
            response = http.responseText;
        }
    };
    http.send();
//    while (response === null) {}
    return response;
}