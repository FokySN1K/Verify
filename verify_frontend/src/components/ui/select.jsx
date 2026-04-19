import React from "react";

const SelectContext = React.createContext(null);

function textFromNode(node) {
  if (node == null) return "";
  if (typeof node === "string" || typeof node === "number") return String(node);
  if (Array.isArray(node)) return node.map(textFromNode).join("");
  if (React.isValidElement(node)) return textFromNode(node.props.children);
  return "";
}

function collectMeta(children, items = [], meta = { placeholder: "" }) {
  React.Children.forEach(children, (child) => {
    if (!React.isValidElement(child)) return;
    if (child.type === SelectItem) {
      items.push({ value: child.props.value, label: textFromNode(child.props.children) });
    }
    if (child.type === SelectValue && child.props.placeholder && !meta.placeholder) {
      meta.placeholder = child.props.placeholder;
    }
    if (child.props?.children) {
      collectMeta(child.props.children, items, meta);
    }
  });
  return { items, placeholder: meta.placeholder };
}

export function Select({ value = "", onValueChange, children }) {
  const meta = React.useMemo(() => collectMeta(children), [children]);
  return (
    <SelectContext.Provider value={{ value, onValueChange, items: meta.items, placeholder: meta.placeholder }}>
      <div>{children}</div>
    </SelectContext.Provider>
  );
}

export function SelectTrigger({ className = "" }) {
  const ctx = React.useContext(SelectContext);
  return (
    <select
      className={`flex h-10 w-full rounded-md border bg-white px-3 py-2 text-sm outline-none focus:ring-2 focus:ring-slate-300 ${className}`.trim()}
      value={ctx?.value ?? ""}
      onChange={(e) => ctx?.onValueChange?.(e.target.value)}
    >
      <option value="" disabled>{ctx?.placeholder || "Выберите значение"}</option>
      {(ctx?.items || []).map((item) => (
        <option key={item.value} value={item.value}>{item.label}</option>
      ))}
    </select>
  );
}

export function SelectValue() {
  return null;
}
export function SelectContent() {
  return null;
}
export function SelectItem() {
  return null;
}
