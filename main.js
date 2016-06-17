$(document).ready(function() {
  doYouLikeMeme.init();
})

var doYouLikeMeme = {
  url: 'http://tiny-tiny.herokuapp.com/collections/doyoulikememe',
  urlmemes: 'http://tiny-tiny.herokuapp.com/collections/doyoulikememevotes',
  comments: [],
  thumbclicks: [],
  init: function() {
    doYouLikeMeme.styling();
    doYouLikeMeme.events();
  },
  styling: function() {
    doYouLikeMeme.getPost();
  },//end styling
  events: function() {
//New Comment
  $('form').submit(function () {
    event.preventDefault();
    if ($('input').val() !== '') {
      var input_value = $(this).find('.messageval').val();
      var user_value = $(this).find('.usernameval').val();
      var thingToCreate = {
        comment: input_value,
        username: user_value
      }
      doYouLikeMeme.createPost(thingToCreate)
      $('.comments').append(`<li>${user_value}${input_value}<a href=""> x</a></li>`);
    };
    $('input').val('');
    return false;
  })
  //delete
  // $(document).on('click', 'a', function (element) {
  //   event.preventDefault();
  //   var commentId = $(this).parent().data('id');
  //   console.log("ID", commentId)
  //   window.glob = $(this);
  //   $(this).parent().remove();
  //   doYouLikeMeme.deletePost(commentId);
  // });

  ////editing a post
  var newLiVal;
  $("ul").on('dblclick', 'li', function () {
    newLiVal = $(this).text();
    $(this).text("");
    $("<input type='text' data-id='"+ $(this).data('id') +"'>").appendTo(this).focus();
  });
  $("ul").on('focusout', 'li > input', function () {
    var $this = $(this);
    var newLiVal = $this.val()
    $this.text($this.val());
    $(this).parent().text($this.val()).append('<a href=""> ✓</a>');
    $this.remove();
    doYouLikeMeme.editPost({
      comment: newLiVal,
      _id: $this.data('id')
    })
  });
  $('.best').on('click', function(){
    event.preventDefault();
    console.log("you clicked best");
  });
  $('.worst').on('click', function(){
    event.preventDefault();
    console.log("you clicked worst");
  });

  $('.thumbsup').on('click', function (event) {
    event.preventDefault();

    var element = event.currentTarget;
    element.clicks = (element.clicks || 0) + 1;
    console.log(element.clicks);
    // .getElementById(data.)
    var thingToPost = {
      clicks: Number(1)
    }

});
  $('.thumbsdown').on('click', function (event) {
    event.preventDefault();
    var element = event.currentTarget;
    // element.clicks = (element.clicks || 0) - 1;
      var total_clicks = element.clicks;
      var thingToPost = {
        clicks: Number(-1)
      }
      doYouLikeMeme.createClick(thingToPost)
  });


/////getting images off of the page1image
      var img = document.getElementsByTagName('img');
      var image= img; /////this returns an array of images
      console.log(image[1]);/////this returns the second image in the array
    },

///begin ajax

  createPost: function(thingCommented) {
    $.ajax({
      url: doYouLikeMeme.url,
      method: "POST",
      data: thingCommented,
      success: function(data) {
        console.log("works", data);
          doYouLikeMeme.comments.push(data);
          doYouLikeMeme.getPost();
      },
      error: function(err) {
        console.error("OH CRAP", err);
      }
    })
  },
  createClick: function(thingClicked) {
    $.ajax({
      url: doYouLikeMeme.urlmemes,
      method: "POST",
      data: thingClicked,
      success: function(data) {
        console.log("works", data);
          doYouLikeMeme.thumbclicks.push(data);
          doYouLikeMeme.getPost();
      },
      error: function(err) {
        console.error("OH CRAP", err);
      }
    })
  },

  getPost: function() {
    $.ajax({
      url: doYouLikeMeme.url,
      method: "GET",
      success: function(data) {
        console.log("WE GOT SOMETHING", data);
        $(".totalcomments").find('h5').text("Total Comments: " + data.length ); ///num of list items
        $('.comments').html("");
        data.forEach(function(element,idx) {
          var toDoStr = `<li data-id="${element._id}"> ${element.username}:                     ${element.comment}<a href=""> x</a></li>`
          $('.comments').append(toDoStr)
          doYouLikeMeme.comments.push(element);
        });
      },
      error: function(err) {
        console.error("ugh", err);
      }
    })
  },
  getClick: function() {
    $.ajax({
      url: doYouLikeMeme.urlmemes,
      method: "GET",
      success: function(data) {
        console.log("WE GOT SOMETHING", data);
          doYouLikeMeme.thumbclicks.push('');
        // });
      },
      error: function(err) {
        console.error("ugh", err);
      }
    })
  },
  deletePost: function(commentId) {
    var DeletePost = doYouLikeMeme.url + "/" + commentId;
      $.ajax({
        url: DeletePost,
        method: "DELETE",
        success: function(data) {
          console.log("WE DELETED SOMETHING", data);
          doYouLikeMeme.getPost();

        },
        error: function(err) {
          console.error("ugh", err);
        }
      })
    },

    editPost: function(thingToEdit) {
      console.log("THING TO EDIT", thingToEdit);
      $.ajax({
        url: doYouLikeMeme.url + "/" + thingToEdit._id,
        method: "PUT",
        data: thingToEdit,
        success: function(data) {
          console.log("edited!!!!!",data)
          doYouLikeMeme.getPost();
        },
        error: function(err) {
          console.error("OH CRAP", err);
        }
      })
  },


};




// var objToSave {
//
//  id: $(img).data('id')
//  up: 1,
//  down: 0
// }

// $('img')[0].src = "http://www.fillmurray.com/200/300"
