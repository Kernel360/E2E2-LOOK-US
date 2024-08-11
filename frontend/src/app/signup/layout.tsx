import { Metadata } from "next"

import {
  PageHeader,
  PageHeaderDescription,
  PageHeaderHeading,
} from "@/components/page-header"

export const metadata: Metadata = {
  title: "Forms",
  description: "Advanced form example using react-hook-form and Zod.",
}

interface ExamplesLayoutProps {
  children: React.ReactNode
}

export default function SignUpLayout({ children }: ExamplesLayoutProps) {
  return (
    <html>
    <body>
      <div className="container relative">
        <PageHeader>
          <PageHeaderHeading className="hidden md:block">
            Check out some examples
          </PageHeaderHeading>
          <PageHeaderHeading className="md:hidden">Examples</PageHeaderHeading>
          <PageHeaderDescription>
            Dashboard, cards, authentication. Some examples built using the
            components. Use this as a guide to build your own.
          </PageHeaderDescription>
        </PageHeader>
        <section>
          <div className="overflow-hidden rounded-[0.5rem] border bg-background shadow-md md:shadow-xl">
            {children}
          </div>
        </section>
      </div>
    </body>
  </html>
  )
}