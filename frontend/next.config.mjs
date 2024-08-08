/** @type {import('next').NextConfig} */

import path from 'path';
import { fileURLToPath } from "url";

const __dirname = fileURLToPath(new URL(".", import.meta.url));
const __filename = fileURLToPath(import.meta.url); 

const nextConfig = {
    async redirects() {
        return [
            {
                source: '/',
                destination: '/posts',
                permanent: true
            }
        ]
    },
    sassOptions: {
        includePaths: [path.join(__dirname, 'styles')],
    },
};

export default nextConfig;
