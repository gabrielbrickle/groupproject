////Clicking on the Top Memes Tab from Nav Bar
$('.best').on('click', function() {
    event.preventDefault();
    function randomMeme(anyMemes){
        var randomNumber= Math.random();
        var howManyMemes= anyMemes.length;
        var guessMeme= Math.floor(randomNumber * howManyMemes);
        return anyMemes[guessMeme];
      }
    console.log("you clicked best");
    $('.fivepics').removeClass('hidden');
    $('.images').addClass('hidden');
    $('.thumbs').addClass('hidden');
    $('.commentforms').addClass('hidden');
    $('h2').text("Top Memes");
    document.getElementById("image1").src=randomMeme(imageurl);
    document.getElementById("image2").src=randomMeme(imageurl);
    document.getElementById("image3").src=randomMeme(imageurl);
    document.getElementById("image4").src=randomMeme(imageurl);
    document.getElementById("image5").src=randomMeme(imageurl);
});
////Clicking on the Worst Memes Tab from Nav Bar
$('.worst').on('click', function() {
    event.preventDefault();
    function randomMeme(anyMemes){
        var randomNumber= Math.random();
        var howManyMemes= anyMemes.length;
        var guessMeme= Math.floor(randomNumber * howManyMemes);
        return anyMemes[guessMeme];
      }
    console.log("you clicked worst");
    $('.fivepics').removeClass('hidden');
    $('.images').addClass('hidden');
    $('.thumbs').addClass('hidden');
    $('.commentforms').addClass('hidden');
    $('h2').text("Worst Memes");
    document.getElementById("image1").src=randomMeme(imageurl);
    document.getElementById("image2").src=randomMeme(imageurl);
    document.getElementById("image3").src=randomMeme(imageurl);
    document.getElementById("image4").src=randomMeme(imageurl);
    document.getElementById("image5").src=randomMeme(imageurl);
});
