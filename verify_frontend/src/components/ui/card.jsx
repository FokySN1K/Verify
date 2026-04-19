import React from "react";

export function Card({ className = "", ...props }) {
  return <div className={`rounded-xl border bg-white ${className}`.trim()} {...props} />;
}
export function CardHeader({ className = "", ...props }) {
  return <div className={`p-6 pb-3 ${className}`.trim()} {...props} />;
}
export function CardTitle({ className = "", ...props }) {
  return <h3 className={`text-lg font-semibold ${className}`.trim()} {...props} />;
}
export function CardDescription({ className = "", ...props }) {
  return <p className={`text-sm text-slate-500 ${className}`.trim()} {...props} />;
}
export function CardContent({ className = "", ...props }) {
  return <div className={`p-6 pt-3 ${className}`.trim()} {...props} />;
}
