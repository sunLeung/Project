// Some general UI pack related JS
// Extend JS String with repeat method
String.prototype.repeat = function(num) {
  return new Array(num + 1).join(this);
};

(function($) {


  $(function() {

    // Tags Input
    //$(".tagsinput").tagsInput();

    // Placeholders for input/textarea
    $(":text, textarea").placeholder();

    // Focus state for append/prepend inputs
    $('.input-group').on('focus', '.form-control', function () {
      $(this).closest('.input-group, .form-group').addClass('focus');
    }).on('blur', '.form-control', function () {
      $(this).closest('.input-group, .form-group').removeClass('focus');
    });
    
    $('.nav-sidebar li').on('click',function(){
    	$(this).parent().find('li').each(function(){
    		$(this).removeClass('active');
    	});
    	$(this).addClass('active');
    });

  });
  
})(jQuery);