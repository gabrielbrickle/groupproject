var gulp = require('gulp');
var uglify = require('gulp-uglify');
var sass = require('gulp-sass');
var htmlmin = require('gulp-htmlmin');
var babel = require('gulp-babel');
var concat = require('gulp-concat');

// gulp.task('default', ['html', 'css', 'js', 'minify']);

gulp.task('watch', function () {
    gulp.watch('./js/*.js', ['js']);
    gulp.watch('./sass/styles.scss', ['css']);
    gulp.watch('./index.html', ['html']);
})
////Removing the white space from HTML file
gulp.task('minify', function() {
  return gulp.src('./index.html')
        .pipe(htmlmin({collapseWhitespace: true}))
        .pipe(gulp.dest('./public'));
});

gulp.task('css', function () {
    gulp.src('./sass/styles.scss')
        .pipe(sass())
        .pipe(gulp.dest('./public'));
});

gulp.task('js', function () {
    gulp.src('./js/*.js')
        .pipe(concat('./js/*.js'))
        .pipe(babel({
             presets: ['es2015']
             }))
        .pipe(uglify())
        .pipe(gulp.dest('./public'));
});
