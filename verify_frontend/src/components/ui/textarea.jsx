import React from "react";

export const Textarea = React.forwardRef(function Textarea({ className = "", ...props }, ref) {
  return (
    <textarea
      ref={ref}
      className={`min-h-[120px] w-full rounded-md border bg-white px-3 py-2 text-sm outline-none focus:ring-2 focus:ring-slate-300 ${className}`.trim()}
      {...props}
    />
  );
});
