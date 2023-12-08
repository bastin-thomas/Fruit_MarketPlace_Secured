//MAIN
var articles = GetArticles();

articles.forEach(article => {
    $("main").append(article.toCard());
});




//Library:

//EventManagementOnClick
function OnButtonUpdateArticleClick(id){
    
    const returnTag = "#return_"+id;

    const prixTag = "#prix_"+id;
    const stockTag = "#stock_"+id;

    //GetUpdatedValue
    let prix = $(prixTag).val();
    let stock = $(stockTag).val();

    //Get others value
    let toUpdate = null;
    articles.forEach(article => {
        if(article.id == id){
            toUpdate = article;
        }
    });

    if(toUpdate === null || toUpdate.prix == prix && toUpdate.stock == stock){
        console.log("Nothing changed");
        $(returnTag).text(`Nothing Changed`);
        
        setTimeout(() => {
            $(returnTag).text(``);
        }, 2000);

        return;
    }

    console.log("Update Card: id(" + id + ") = {prix=" + prix + ", stock" + stock + "}");

    //SendPutToServer
    let isUpdated = UpdateArticles(id, prix, stock);

    //Put a success/error update status:
    
    if(isUpdated){
        $(returnTag).text(`Sucessfully Updated`);
        $(returnTag).css("color", "green");
    }
    else{
        $(returnTag).text(`Error during Update`);
        $(returnTag).css("color", "red");
    }
    
    setTimeout(() => {
        RefreshView();
    }, 2000);
}

function RefreshView(){
    articles = GetArticles();

    var main = $("main");
    //resetData
    main.html("");

    //put newData
    articles.forEach(article => {
        main.append(article.toCard());
    });
}