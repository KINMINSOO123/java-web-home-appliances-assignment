<%@ include file="/jsp/header.jsp"%>
  <main class="login-form">
  <div style="margin-top:100px;" class="cotainer">
    <div class="row justify-content-center">
      <div class="col-md-8">
        <div class="card">
          <div class="card-header">
            Customer Registration   
          </div>
          <div class="card-body">
            <form action="CustomerRegisterServletV2" method="get">
                <font color="red">
                        ${errors.database}
                  </font>
              <div style="margin:10px 0px;" class="form-group row">
                <label for="name" 
                  class="col-md-4 col-form-label text-md-right">Name<font color="red"></font></label>
                <div class="col-md-6">
                  <input type="text"   class="form-control" placeholder="Enter Your Name"
                    name="name" value="${param.name}" >  
                </div>
                  <font color="red">
                        ${errors.name}
                  </font>
              </div>
              
              <div style="margin:10px 0px;" class="form-group row">
                <label for="password" 
                  class="col-md-4 col-form-label text-md-right">Password<font color="red"></font></label>
                <div class="col-md-6">
                  <input type="password" class="form-control" placeholder="Enter password"
                    name="password" value="${param.password}" >      
                </div>
                  <font color="red">
                        ${errors.password}
                  </font>
              </div>
              
              <div style="margin:10px 0px;" class="form-group row">
                <label for="email_address" 
                  class="col-md-4 col-form-label text-md-right">Email<font color="red"></font></label>
                <div class="col-md-6">
                  <input type="text"  class="form-control" placeholder="Enter Email Address"
                    name="email" value="${param.email}" >                
                </div>
                  <font  color="red">
                        ${errors.email}
                  </font>
              </div>
              
              <div style="margin:10px 0px;" class="form-group row">
                <label for="phone" 
                  class="col-md-4 col-form-label text-md-right">Mobile No.<font color="red"></font></label>
                <div class="col-md-6">
                  <input id="phone" type="tel" name="phone" value="${param.phone}" class="form-control" placeholder="Enter Mobile No."/>  
                </div>                    
                  <font color="red">
                        ${errors.phone}
                  </font>
              </div>
                
              <div style="margin:10px 0px;" class="form-group row">
                <label for="home-address" 
                  class="col-md-4 col-form-label text-md-right">Home Address<font color="red"></font></label>
                <div class="col-md-6">
                  <textarea class="form-control" placeholder="Enter Home Address"
                    name="address" rows="3">${param.address}</textarea>
                </div>
                  <font color="red">
                    ${errors.address}
                  </font>
              </div>
                  
              <div style="margin:10px 0px;" class="form-group row">
                <label for="shipping-address" 
                  class="col-md-4 col-form-label text-md-right">Shipping Address<font color="red"></font></label>
                <div class="col-md-6">
                  <textarea class="form-control" placeholder="Enter Shipping Address"
                    name="sAddress" rows="3">${param.sAddress}</textarea>
                </div>
                  <font color="red">
                    ${errors.sAddress}
                  </font>
              </div>
              
              <div style="margin:15px 0px;" class="form-group row">
                <label for="dob" 
                  class="col-md-4 col-form-label text-md-right">Date Of Birth<font color="red"></font></label>
                <div class="col-md-6">
                  <input type="date"  id="datepicker" class="form-control" placeholder="Enter Date Of Birth"
                    name="dob" value="${param.dob}" >
                </div>
                  <font  color="red">
                    ${errors.dob}
                  </font>
              </div>
              
              <div style="margin:10px 0px;" class="form-group row">
                <label for="password" 
                  class="col-md-4 col-form-label text-md-right">Confirm Password<font color="red"></font></label>
                <div class="col-md-6">
                  <input type="password"  class="form-control" placeholder="Enter confirm password"
                    name="conPassword" value="${param.conPassword}" > 
                </div>
                  <font  color="red">
                        ${errors.conPassword}
                  </font>
              </div>
              
              <div style="margin:25px 15px 10px;" class="col-md-6 offset-md-4">
                <input type="submit" class="btn btn-primary" name="operation" value="Register">
              </div>
            </form>
          </div>
        </div>
      </div>
    </div>
  </div>
  </main>
  <div style="margin-top: 120px">
      <script src="https://cdn.jsdelivr.net/npm/intl-tel-input@25.3.1/build/js/intlTelInput.min.js"></script>
      <script type="text/javascript" src="js/phone.js"></script>
    <%@ include file="/jsp/footer.jsp"%>
