const { createProxyMiddleware } = require('http-proxy-middleware');
const isDevelopment = process.env.NODE_ENV === 'development';

module.exports = function(app) {
    if (isDevelopment) {
        app.use(
            '/problems-service',
            createProxyMiddleware({
                target: 'http://localhost:8080',
                changeOrigin: true,
                pathRewrite: (path, req) => path.replace('/problems-service', ''),
            })
        );
        app.use(
            '/auth-service',
            createProxyMiddleware({
                target: 'http://localhost:8081',
                changeOrigin: true,
                pathRewrite: (path, req) => path.replace('/auth-service', ''),
            })
        );
        app.use(
            '/discussion-service',
            createProxyMiddleware({
                target: 'http://localhost:8082',
                changeOrigin: true,
                pathRewrite: (path, req) => path.replace('/discussion-service', ''),
            })
        );
    }
};
