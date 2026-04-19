import React from "react";

const DialogContext = React.createContext(null);

export function Dialog({ open, onOpenChange, children }) {
  const [internalOpen, setInternalOpen] = React.useState(false);
  const isControlled = typeof open === "boolean";
  const isOpen = isControlled ? open : internalOpen;
  const setOpen = onOpenChange ?? setInternalOpen;

  return (
    <DialogContext.Provider value={{ open: isOpen, setOpen }}>
      {children}
    </DialogContext.Provider>
  );
}

export function DialogTrigger({ asChild, children }) {
  const ctx = React.useContext(DialogContext);
  const child = React.Children.only(children);
  const onClick = (event) => {
    child.props.onClick?.(event);
    if (!event.defaultPrevented) {
      ctx?.setOpen?.(true);
    }
  };
  return asChild ? React.cloneElement(child, { onClick }) : <button type="button" onClick={onClick}>{children}</button>;
}

export function DialogContent({ className = "", children }) {
  const ctx = React.useContext(DialogContext);
  if (!ctx?.open) return null;

  return (
    <div className="fixed inset-0 z-50 flex items-center justify-center bg-black/40 p-4" onClick={() => ctx.setOpen?.(false)}>
      <div className={`relative max-h-[90vh] w-full overflow-y-auto rounded-xl bg-white p-6 shadow-xl ${className}`.trim()} onClick={(e) => e.stopPropagation()}>
        <button type="button" className="absolute right-3 top-3 rounded border px-2 py-1 text-xs" onClick={() => ctx.setOpen?.(false)}>
          Закрыть
        </button>
        {children}
      </div>
    </div>
  );
}

export function DialogHeader({ className = "", ...props }) {
  return <div className={`mb-4 ${className}`.trim()} {...props} />;
}
export function DialogTitle({ className = "", ...props }) {
  return <h2 className={`text-lg font-semibold ${className}`.trim()} {...props} />;
}
