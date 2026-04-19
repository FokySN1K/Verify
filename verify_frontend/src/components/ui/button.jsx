import React from "react";

const variantClasses = {
  default: "bg-slate-900 text-white hover:bg-slate-800",
  outline: "border bg-white text-slate-900 hover:bg-slate-50",
  ghost: "text-slate-700 hover:bg-slate-100",
};
const sizeClasses = {
  default: "h-10 px-4 py-2",
  sm: "h-9 px-3 py-2 text-sm",
};

export const Button = React.forwardRef(function Button(
  { className = "", variant = "default", size = "default", type = "button", ...props },
  ref
) {
  return (
    <button
      ref={ref}
      type={type}
      className={`inline-flex items-center justify-center gap-2 rounded-md text-sm font-medium transition disabled:pointer-events-none disabled:opacity-50 ${variantClasses[variant] || variantClasses.default} ${sizeClasses[size] || sizeClasses.default} ${className}`.trim()}
      {...props}
    />
  );
});
