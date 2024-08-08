import { Metadata } from "next"
import Link from "next/link"

import { cn } from "@/lib/utils"
import {
  PageActions,
  PageHeader,
  PageHeaderDescription,
  PageHeaderHeading,
} from "@/components/page-header"

import { buttonVariants } from "@/components/ui/button"

export const metadata: Metadata = {
  title: "Examples",
  description: "Check out some examples app built using the components.",
}

interface ExamplesLayoutProps {
  children: React.ReactNode
}

export default function ExamplesLayout({ children }: ExamplesLayoutProps) {
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
