import React from "react";

const TabsContext = React.createContext(null);

export function Tabs({ defaultValue, value, onValueChange, className = "", children, ...props }) {
  const [internalValue, setInternalValue] = React.useState(defaultValue);
  const currentValue = value ?? internalValue;
  const setValue = onValueChange ?? setInternalValue;
  return (
    <TabsContext.Provider value={{ value: currentValue, setValue }}>
      <div className={className} {...props}>{children}</div>
    </TabsContext.Provider>
  );
}
export function TabsList({ className = "", ...props }) {
  return <div className={`inline-flex rounded-lg border bg-white p-1 ${className}`.trim()} {...props} />;
}
export function TabsTrigger({ value, className = "", children, ...props }) {
  const ctx = React.useContext(TabsContext);
  const active = ctx?.value === value;
  return (
    <button
      type="button"
      className={`rounded-md px-3 py-1.5 text-sm ${active ? "bg-slate-900 text-white" : "text-slate-700 hover:bg-slate-100"} ${className}`.trim()}
      onClick={() => ctx?.setValue?.(value)}
      {...props}
    >
      {children}
    </button>
  );
}
export function TabsContent({ value, className = "", children, ...props }) {
  const ctx = React.useContext(TabsContext);
  if (ctx?.value !== value) return null;
  return <div className={className} {...props}>{children}</div>;
}
