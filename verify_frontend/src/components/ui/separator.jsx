import React from "react";

export function Separator({ className = "", ...props }) {
  return <hr className={`border-slate-200 ${className}`.trim()} {...props} />;
}
