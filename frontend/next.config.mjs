/** @type {import('next').NextConfig} */
const nextConfig = {
    async redirects() {
        return [
            {
                source: '/',
                destination: '/posts',
                permanent: true
            }
        ]
    }
};

export default nextConfig;
