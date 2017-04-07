$(function() {
  $.ajax({
    url: "http://localhost:9000/api/category/list"
  }).done(function(rootCategories){
    var categories=$("#categories");
    for(var i=0;i<rootCategories.length;i++) {
      var dropdown=$('<div class="dropdown-menu" aria-labelledby="navbarDropdownMenuLink"></div>');
      $('<li class="nav-item dropdown"><a class="nav-link dropdown-toggle" href="#" id="navbarDropdownMenuLink" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">'+
      rootCategories[i].displayName+'</a></li>').append(dropdown).appendTo(categories);
      buildDropdown(rootCategories[i].name, dropdown);
    }
  }).fail(function(jqXHR, textStatus){
    console.error("Couldn't load root categories", jqXHR);
  });

  if(location.hash) {
    loadProducts(location.hash.replace("#",""));
  }

  window.onhashchange=function() {
    loadProducts(location.hash.replace("#",""));
  };

  $("#authForm").submit(function (event) {
    var username=$("#username").val();
    var password=$("#password").val();

    var baseAuth=btoa(username+":"+password);

    $.ajax({
      url: "http://localhost:9000/api/authorization/token",
      contentType: "text/plain",
      headers:{Authorization: "Basic "+baseAuth},
    }).done(function(token){
      if (typeof(Storage) !== "undefined") {
        localStorage.token=token;
      } else {
        console.error(" Sorry! No Web Storage support..");
      }
    }).fail(function(jqXHR, textStatus) {
      console.error("Couldn't authorize: ", jqXHR);
    });
    event.preventDefault();
  });

  function buildDropdown(category,target) {
    $.ajax({
      url: "http://localhost:9000/api/category/list/"+category
    }).done(function(childCategories){
      for(var i=0;i<childCategories.length;i++) {
        target.append($('<a class="dropdown-item" href="#'+childCategories[i].name+'">'+childCategories[i].displayName+'</a>'));
      }
    });
  }

  function loadProducts(category) {
    $.ajax({
          url: "http://localhost:9000/api/product/list",
          method: "POST",
          contentType: "application/json",
          data:'{"category":"'+category+'", "properties":[]}',
      }).done(function(products) {
        $("#products > tbody").empty();
        for( var i=0;i<products.length;i++)
          $("#products  > tbody:last-child").append("<tr><td>"+
            products[i].displayName+"</td><td>"+
            products[i].price+"</td></tr>");
      }).fail(function(jqXHR, textStatus) {
        console.error("Couldn't load products: ", jqXHR);
      });
  }
});
