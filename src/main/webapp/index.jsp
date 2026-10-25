<%@ include file="jsp/header.jsp"%>
<%! String name = "User"; %>
<%  if(isLoggedIn){
        name = customer.getCustomerName();
    }
%>
<div class="home-content-wrapper">
    <div class="overlay-box">
        <div class="welcome-text">
            WELCOME TO <br/> HOME & Co.
            ${error}
        </div>
    </div>
</div>
<!--<div><p>error</p></div>-->
<div id="carouselExampleDark" class="carousel carousel-dark slide">
  <div class="carousel-indicators">
    <button type="button" data-bs-target="#carouselExampleDark" data-bs-slide-to="0" class="slider-btn active" aria-current="true" aria-label="Slide 1"></button>
    <button type="button" class="slider-btn" data-bs-target="#carouselExampleDark" data-bs-slide-to="1" aria-label="Slide 2"></button>
    <button type="button" class="slider-btn" data-bs-target="#carouselExampleDark" data-bs-slide-to="2" aria-label="Slide 3"></button>
  </div>
    <div class="pricing-header px-3 py-3 pt-md-5 pb-md-4 mx-auto text-center">
      <h1 class="display-4">Hello <%= name %></h1>
      <p class="lead">Welcome to Home & Co., your trusted destination for all things home appliances. Whether you're upgrading your kitchen, renovating your laundry room, or simply replacing an old appliance, we make it easy to find exactly what you need?all in one place. Our carefully curated selection includes top brands known for their performance, durability, and innovation. From energy-efficient refrigerators and powerful washing machines to compact microwaves and smart air purifiers, we bring you the best in modern home solutions.

At Home & Co., we believe that shopping for appliances should be as convenient and stress-free as using them. That?s why we?ve built a user-friendly platform with detailed product descriptions, honest customer reviews, and helpful filters to guide your search. Whether you're looking for high-end technology or budget-friendly basics, you?ll find a wide range of options to suit your lifestyle and home.

Customer satisfaction is at the heart of everything we do. We offer fast and reliable shipping, secure payment options, and dedicated support to ensure a smooth experience from checkout to delivery. Plus, with regular deals, bundle offers, and seasonal promotions, you can enjoy top-quality appliances without breaking the bank.

Join thousands of happy customers who have transformed their homes with us. Explore our collections today and discover how the right appliances can make everyday living easier, more efficient, and a lot more enjoyable. Your home deserves the best?and we?re here to help you find it.</p>
    </div>
  <div class="carousel-inner">
    <div class="carousel-item active" data-bs-interval="10000">
      <img src="image/bed-slider.jpg" height=600 class="d-block w-100" alt="...">
      <div class="carousel-caption d-none d-md-block">
        <h5 class="slider-title">Bed room appliances and accessories</h5>
        <p class="slider-desc">The bedroom is a personal sanctuary, and the right appliances and accessories can enhance comfort and convenience. </p>
      </div>
    </div>
    <div class="carousel-item" data-bs-interval="2000">
      <img src="image/kitchen-slider.jpg" height=600 class="d-block w-100" alt="...">
      <div class="carousel-caption d-none d-md-block">
        <h5 class="slider-title">Kitchen appliances and accessories</h5>
        <p class="slider-desc">A well-equipped kitchen makes cooking easier and more enjoyable.</p>
      </div>
    </div>
    <div class="carousel-item">
      <img src="image/living-slider.jpg" height=600 class="d-block w-100" alt="...">
      <div class="carousel-caption d-none d-md-block">
        <h5 class="slider-title">Living room appliances and accessories</h5>
        <p class="slider-desc">The living room is the heart of the home, a space for relaxation and entertainment. </p>
      </div>
    </div>
  </div>
  <button class="carousel-control-prev" type="button" data-bs-target="#carouselExampleDark" data-bs-slide="prev">
    <span class="carousel-control-prev-icon" aria-hidden="true"></span>
    <span class="visually-hidden">Previous</span>
  </button>
  <button class="carousel-control-next" type="button" data-bs-target="#carouselExampleDark" data-bs-slide="next">
    <span class="carousel-control-next-icon" aria-hidden="true"></span>
    <span class="visually-hidden">Next</span>
  </button>
</div>
<%@ include file="jsp/footer.jsp"%>
