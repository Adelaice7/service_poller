const path = require('path');
const webpack = require('webpack');
const ROOT =  path.resolve(__dirname, './src/main/resources');
const DEST = path.resolve(__dirname, './src/main/resources/static/js/dist');
const MiniCssExtractPlugin = require('mini-css-extract-plugin');

module.exports = {
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
        new MiniCssExtractPlugin({
            filename: "dist/[name].css",
            chunkFilename: "dist/[id].css"
        }),
        new webpack.ProvidePlugin({
            $: "jquery",
            jQuery: "jquery"
        })
    ],
}
