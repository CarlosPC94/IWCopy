export default class Product {
    constructor(name,price,discount, quantity,imglink, shortDesc, longDesc, category) {
      this.name =name;
      this.price=price;
      this.discount=discount;
      this.quantity=quantity;
      this.imglink=imglink;
      this.short_desc=shortDesc;
      this.long_desc=longDesc;
      this.category=category;
     
    }
}