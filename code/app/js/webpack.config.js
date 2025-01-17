const path = require('path');

module.exports = {
    mode: 'development',
    resolve: {
        extensions: ['.tsx', '.ts', '.js', 'css'],
    },
    module: {
        rules: [
            {
                test: /.tsx?$/,
                use: 'ts-loader',
                exclude: /node_modules/,
            },
            {
                test: /\.(png|jpeg|gif|jpg)$/i,
                use: [
                    {
                        loader: 'file-loader',
                        options: {
                            name: '[name].[ext]',
                            outputPath: 'images', // Output path for images
                        },
                    },
                ],
              },
              {
                test: /\.css$/,
                use: ['style-loader', 'css-loader'],
                include: /css/
              },
        ],
    },
    devServer: {
        static: path.resolve(__dirname, 'dist'),
        proxy: {
            '/api': {
                target: 'http://localhost:8080',
                pathRewrite: { '^/api': '' },
            },
        },
        port: 8081,
        historyApiFallback: true,
        headers: {
            'Access-Control-Allow-Origin': '*',
        },
    },
};
