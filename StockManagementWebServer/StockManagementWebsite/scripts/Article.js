class Article {
    constructor(json) {

      this.id = json.id;
      this.intitule = json.intitule;
      this.prix = json.prix;
      this.stock = json.stock;
      this.image = json.image;


      this.toString = function() {
        return "(Id=" + this.id + ", intitule=" + this.intitule + ", prix=" + this.prix + ", stock=" + this.stock + ", image=" + this.image + ")";
      };

      this.toRow = function(){
        return `<tr class="tablerows">
                    <td> ${this.id} </td>
                    <td> ${this.intitule} </td>
                    <td> ${this.prix} </td>
                    <td> ${this.stock} </td>
                    <td> ${this.image} </td>
                </tr>`;
      };


      this.toCard = function(){
        return `
        <div class="card mb-3" id="card_${this.id}">
            <img src="./img/${this.image}" class="card-img-top" alt="image of the ${this.intitule}">

            <div class="card-body">
                <h5 class="card-title">${this.intitule}</h5>
                <div class="card-text">
                    <form>
                        <div class="form-group">
                            <label>Prix:</label>
                            <input class="form-control" type="number" id="prix_${this.id}" min="0" step="0.01" value="${this.prix}"/>
                        </div>

                        <div class="form-group">
                            <label>Stock:</label>
                            <input class="form-control" type="number" id="stock_${this.id}" min="0" step="1" value="${this.stock}"/>
                        </div>
                    </form>
                </div>
                <div class="btn_group">
                  <button class="btn btn-primary" onclick="OnButtonUpdateArticleClick(${this.id})">Update</button>
                  <p id="return_${this.id}"></p>
                </div>
            </div>
        </div>
        `;        
      };
    }
  }