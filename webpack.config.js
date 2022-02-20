const path = require('path');
const ROOT =  path.resolve(__dirname, './src/main/resources');
const DEST = path.resolve(__dirname, './src/main/resources/static/js/dist');
const MiniCssExtractPlugin = require('mini-css-extract-plugin');
const HtmlWebpackPlugin = require('html-webpack-plugin');

module.exports = {
    mode: 'development',
    entry: [
        path.resolve(ROOT, './static/js/app.js'),
    ],
    output: {
        path: DEST,
        filename: './bundle.js'
    },
    module: {
        rules: [
            {
                test: /\.css$/,
                use: ['style-loader', 'css-loader']
            },
            {
                test: /\.s[ac]ss$/i,
                use: [{
                    loader: 'style-loader', // inject CSS to page
                }, {
                    loader: 'css-loader', // translates CSS into CommonJS modules
                }, {
                    loader: 'sass-loader' // compiles Sass to CSS
                }]
            },
            {
                test: /\.m?js$/,
                exclude: /(node_modules|bower_components)/,
                use: {
                    loader: 'babel-loader'
                }
            },
        ],
    },
    resolve: {
        extensions: ['.js', '.jsx']
    },
    plugins: [
        // Extracts CSS into a dedicated css files
        new MiniCssExtractPlugin({
            filename: "dist/[name].css",
            chunkFilename: "dist/[id].css"
        })
    ],
}
