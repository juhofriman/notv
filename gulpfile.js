var gulp = require('gulp');
var webpack = require('webpack-stream');
var HtmlWebpackPlugin = require('html-webpack-plugin')

gulp.task('default', function() {
  return gulp.src('frontend/src/app.jsx')
    .pipe(webpack({
      devtool: 'source-map',
      watch: true,
      module: {
        loaders: [
            {
              //tell webpack to use jsx-loader for all *.jsx files
              test: /\.jsx$/,
              loader: 'jsx-loader?insertPragma=React.DOM&harmony'
            },
            {
              test: /\.scss$/,
              loaders: ["style", "css", "sass"]
            }
        ]
      },
      plugins: [new HtmlWebpackPlugin({
        title: 'NOTV',
        template: 'frontend/index.ejs', // Load a custom template
        inject: 'body' // Inject all scripts into the body
      })]
    }
  )).pipe(gulp.dest('resources/public'));
});
