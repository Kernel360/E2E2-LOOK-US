import { Metadata } from "next"

import {
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
        <div className="container relative">
          <section>
            <div className="overflow-hidden rounded-[0.5rem] border bg-background shadow-md md:shadow-xl">
              {children}
            </div>
          </section>
        </div>
  )
}
