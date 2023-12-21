/**
 * 
 */
function GetArticles(){
    const xhr = new XMLHttpRequest();
    xhr.open("GET", "http://127.0.0.1/api/v1/Articles?cleSession=-1", false);
    xhr.send();
    
    if (xhr.readyState == 4 && xhr.status == 200) {
        const data = JSON.parse(xhr.response);
        
        if(data.Articles === undefined){
           console.log("Impossible to getData:" + Cause);
           return null;
        }

        let Articles = data.Articles
        
        var ArticlesArray = [];

        Articles.forEach(data => {
            ArticlesArray.push(new Article(data));
        });

        return ArticlesArray;
    } else {
        console.log(`Error: ${xhr.status}`);
        return null;
    }
}



/**
 * 
 * @param {*} data 
 */
function UpdateArticles(id, prix, stock){
    const xhr = new XMLHttpRequest();

    xhr.open("POST", "http://127.0.0.1/api/v1/Articles", false);
    xhr.setRequestHeader("Content-type","application/x-www-form-urlencoded");

    xhr.send(new URLSearchParams({
        'cleSession': '-1',
        'idArticle': id,
        'prix': prix,
        'stock': stock
    }));
    
    if (xhr.readyState == 4 && xhr.status == 200) {
        const data = JSON.parse(xhr.response);

        if(data.Cause !== undefined){
            console.log("Impossible to UpdateData:" + Cause);
            return null;
        }

        return data.Success;
    } else {
        console.log(`Error: ${xhr.status}`);
        return null;
    }
}