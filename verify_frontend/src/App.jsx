import React, { useEffect, useMemo, useState } from "react";
import { diffLines } from "diff";
import { Card, CardContent, CardHeader, CardTitle, CardDescription } from "@/components/ui/card";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { Label } from "@/components/ui/label";
import { Textarea } from "@/components/ui/textarea";
import { Badge } from "@/components/ui/badge";
import { Tabs, TabsContent, TabsList, TabsTrigger } from "@/components/ui/tabs";
import { Dialog, DialogContent, DialogHeader, DialogTitle, DialogTrigger } from "@/components/ui/dialog";
import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from "@/components/ui/select";
import { ScrollArea } from "@/components/ui/scroll-area";
import { Separator } from "@/components/ui/separator";
import {
  FileText,
  FolderKanban,
  RefreshCcw,
  Plus,
  Upload,
  UserRound,
  PencilLine,
  ChevronDown,
  ChevronRight,
  UserPlus,
} from "lucide-react";

const API_BASE_URL = (import.meta.env.VITE_API_BASE_URL || "http://localhost:8080").replace(/\/$/, "");

const XML_STATUSES = ["NEW", "PROCESSING", "CHECKING", "OLD", "DONE", "REFUSED"];
const CONTRACT_STATUSES = ["NEW", "TENDER", "PROCESSING", "DONE", "FAILED"];

const PRESET_USERS = [
  { client_id: "customer_1", name: "Иван", surname: "Заказчиков", role: "CUSTOMER", email: "customer1@example.com" },
  { client_id: "customer_2", name: "Мария", surname: "Клиентова", role: "CUSTOMER", email: "customer2@example.com" },
  { client_id: "contractor_1", name: "Пётр", surname: "Подрядчиков", role: "CONTRACTOR", email: "contractor1@example.com" },
  { client_id: "contractor_2", name: "Олег", surname: "Исполнителев", role: "CONTRACTOR", email: "contractor2@example.com" },
];

async function post(path, body) {
  const response = await fetch(`${API_BASE_URL}${path}`, {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(body),
  });

  const text = await response.text();
  let data = {};
  try {
    data = text ? JSON.parse(text) : {};
  } catch {
    data = { raw: text };
  }

  if (!response.ok) {
    throw new Error(data?.message || `HTTP ${response.status}`);
  }

  if (typeof data?.code === "number" && data.code !== 0) {
    throw new Error(data?.message || "Ошибка API");
  }

  return data;
}

function buildDiffRows(oldText, newText) {
  const parts = diffLines(oldText || "", newText || "");
  return parts.flatMap((part, index) => {
    const lines = part.value.split("\n");
    if (lines[lines.length - 1] === "") lines.pop();
    return lines.map((line, lineIndex) => ({
      id: `${index}-${lineIndex}`,
      type: part.added ? "added" : part.removed ? "removed" : "unchanged",
      text: line || " ",
    }));
  });
}

function formatXml(xml) {
  if (!xml || typeof xml !== "string") return "";

  try {
    const parser = new DOMParser();
    const parsed = parser.parseFromString(xml, "application/xml");
    if (parsed.querySelector("parsererror")) return xml;

    const serializer = new XMLSerializer();
    const raw = serializer.serializeToString(parsed);
    const withBreaks = raw.replace(/(>)(<)(\/*)/g, "$1\n$2$3");
    const lines = withBreaks.split("\n");

    let indent = 0;
    const formatted = lines
        .map((line) => line.trim())
        .filter(Boolean)
        .map((line) => {
          if (/^<\/[^>]+>/.test(line)) {
            indent = Math.max(indent - 1, 0);
          }

          const result = `${"  ".repeat(indent)}${line}`;

          if (/^<[^!?/][^>]*[^/]>$/.test(line) && !line.includes("</")) {
            indent += 1;
          }

          return result;
        });

    return formatted.join("\n");
  } catch {
    return xml;
  }
}

function groupByContract(xmlLightList, contractList) {
  const map = new Map();

  for (const contract of contractList || []) {
    map.set(contract.id, { ...contract, xmlGroups: [] });
  }

  for (const xml of xmlLightList || []) {
    const contract = xml.contract;
    if (!contract) continue;

    if (!map.has(contract.id)) {
      map.set(contract.id, { ...contract, xmlGroups: [] });
    }

    const item = map.get(contract.id);
    item.xmlGroups.push(xml);
  }

  const contracts = Array.from(map.values());

  return contracts
      .map((contract) => {
        const xmlByName = contract.xmlGroups.reduce((acc, xml) => {
          const key = `${xml.name}__${xml.xsd_light?.id ?? "no-xsd"}`;
          if (!acc[key]) {
            acc[key] = {
              xml_name: xml.name,
              xsd_id: xml.xsd_light?.id,
              xsd_name: xml.xsd_light?.name,
              xsd_stage: xml.xsd_light?.stage,
              versions: [],
            };
          }
          acc[key].versions.push(xml);
          return acc;
        }, {});

        const xmlDocuments = Object.values(xmlByName).map((group) => ({
          ...group,
          versions: group.versions.sort((a, b) => (b.version ?? 0) - (a.version ?? 0)),
        }));

        return { ...contract, xmlDocuments };
      })
      .sort((a, b) => b.id - a.id);
}

function ErrorBox({ error }) {
  if (!error) return null;
  return (
      <div className="max-h-40 overflow-y-auto rounded-md border border-red-200 bg-red-50 p-3 text-sm text-red-600 whitespace-pre-wrap break-all">
        {error}
      </div>
  );
}

function getStatusAppearance(status) {
  const normalized = (status || "").toUpperCase();

  if (normalized === "DONE") {
    return {
      badge: "bg-emerald-100 text-emerald-800 border-emerald-200",
      bar: "bg-emerald-500",
    };
  }

  if (normalized === "REFUSED" || normalized === "FAILED") {
    return {
      badge: "bg-red-100 text-red-800 border-red-200",
      bar: "bg-red-500",
    };
  }

  if (normalized === "PROCESSING" || normalized === "CHECKING" || normalized === "TENDER") {
    return {
      badge: "bg-amber-100 text-amber-800 border-amber-200",
      bar: "bg-amber-500",
    };
  }

  if (normalized === "NEW") {
    return {
      badge: "bg-blue-100 text-blue-800 border-blue-200",
      bar: "bg-blue-500",
    };
  }

  if (normalized === "OLD") {
    return {
      badge: "bg-slate-200 text-slate-700 border-slate-300",
      bar: "bg-slate-400",
    };
  }

  return {
    badge: "bg-slate-100 text-slate-800 border-slate-200",
    bar: "bg-slate-300",
  };
}

function StatusBadge({ value }) {
  const appearance = getStatusAppearance(value);
  return (
      <Badge variant="outline" className={appearance.badge}>
        {value || "—"}
      </Badge>
  );
}

function Field({ label, value }) {
  return (
      <div className="space-y-1 min-w-0 overflow-hidden">
        <div className="text-xs text-slate-500">{label}</div>
        <div className="text-sm font-medium break-all whitespace-pre-wrap max-w-full">
          {value || "—"}
        </div>
      </div>
  );
}

function AppHeader({ currentUser, onLogout, onRefresh, loading }) {
  return (
      <div className="flex flex-col gap-4 md:flex-row md:items-center md:justify-between">
        <div>
          <h1 className="text-3xl font-semibold tracking-tight">XML Contracts</h1>
          <p className="text-sm text-slate-500 mt-1">Минимальный фронтенд для заказчиков и подрядчиков</p>
        </div>
        {currentUser && (
            <div className="flex flex-wrap items-center gap-2">
              <Badge className="rounded-full px-3 py-1">{currentUser.role}</Badge>
              <div className="text-sm text-slate-600 flex items-center gap-2">
                <UserRound className="h-4 w-4" />
                {currentUser.name} {currentUser.surname}
              </div>
              <Button variant="outline" onClick={onRefresh} disabled={loading}>
                <RefreshCcw className="h-4 w-4 mr-2" /> Обновить
              </Button>
              <Button variant="ghost" onClick={onLogout}>Выйти</Button>
            </div>
        )}
      </div>
  );
}

function LoginScreen({ onSelect }) {
  return (
      <div className="min-h-screen bg-slate-50 p-6 md:p-10">
        <div className="max-w-5xl mx-auto space-y-8">
          <AppHeader />
          <Card className="rounded-2xl shadow-sm">
            <CardHeader>
              <CardTitle>Выбор пользователя</CardTitle>
              <CardDescription>
                Для MVP не делаем авторизацию. Выбираем заранее созданного клиента из базы.
              </CardDescription>
            </CardHeader>
            <CardContent className="grid md:grid-cols-2 gap-4">
              {PRESET_USERS.map((user) => (
                  <button
                      key={user.client_id}
                      onClick={() => onSelect(user)}
                      className="text-left rounded-2xl border bg-white p-4 hover:shadow-md transition"
                  >
                    <div className="flex items-center justify-between gap-4">
                      <div>
                        <div className="text-lg font-semibold">{user.name} {user.surname}</div>
                        <div className="text-sm text-slate-500 mt-1">{user.email}</div>
                      </div>
                      <Badge>{user.role}</Badge>
                    </div>
                    <div className="text-xs text-slate-500 mt-3">client_id: {user.client_id}</div>
                  </button>
              ))}
            </CardContent>
          </Card>
        </div>
      </div>
  );
}

function CreateContractDialog({ currentUser, onCreated }) {
  const [open, setOpen] = useState(false);
  const [name, setName] = useState("");
  const [description, setDescription] = useState("");
  const [contractorId, setContractorId] = useState("");
  const [submitting, setSubmitting] = useState(false);
  const [error, setError] = useState("");

  const contractors = PRESET_USERS.filter((user) => user.role === "CONTRACTOR");

  async function handleSubmit() {
    setSubmitting(true);
    setError("");
    try {
      await post("/customer/create_contract", {
        client_id: currentUser.client_id,
        contract_name: name,
        contract_description: description,
      });

      const contractsData = await post("/contract/get_contracts", {
        client_id: currentUser.client_id,
      });

      const createdContract = (contractsData.contract_list || [])
          .filter((item) => item.name === name)
          .sort((a, b) => b.id - a.id)[0];

      if (contractorId && createdContract?.id) {
        await post("/customer/set_contractor", {
          customer_client_id: currentUser.client_id,
          contract_id: createdContract.id,
          contractor_client_id: contractorId,
        });
      }

      setName("");
      setDescription("");
      setContractorId("");
      setOpen(false);
      onCreated?.();
    } catch (e) {
      setError(e.message);
    } finally {
      setSubmitting(false);
    }
  }

  return (
      <Dialog open={open} onOpenChange={setOpen}>
        <DialogTrigger asChild>
          <Button>
            <Plus className="h-4 w-4 mr-2" /> Новый заказ
          </Button>
        </DialogTrigger>
        <DialogContent className="sm:max-w-lg max-w-[calc(100vw-2rem)] rounded-2xl overflow-hidden">
          <DialogHeader>
            <DialogTitle>Создать заказ</DialogTitle>
          </DialogHeader>
          <div className="space-y-4 min-w-0">
            <div className="space-y-2">
              <Label>Название</Label>
              <Input value={name} onChange={(e) => setName(e.target.value)} placeholder="Например, Договор №25" />
            </div>
            <div className="space-y-2">
              <Label>Описание</Label>
              <Textarea value={description} onChange={(e) => setDescription(e.target.value)} placeholder="Короткое описание заказа" />
            </div>
            <div className="space-y-2">
              <Label>Подрядчик</Label>
              <Select value={contractorId} onValueChange={setContractorId}>
                <SelectTrigger>
                  <SelectValue placeholder="Можно не выбирать сразу" />
                </SelectTrigger>
                <SelectContent>
                  {contractors.map((contractor) => (
                      <SelectItem key={contractor.client_id} value={contractor.client_id}>
                        {contractor.name} {contractor.surname}
                      </SelectItem>
                  ))}
                </SelectContent>
              </Select>
            </div>
            <ErrorBox error={error} />
            <Button onClick={handleSubmit} disabled={submitting || !name.trim()} className="w-full">
              Создать
            </Button>
          </div>
        </DialogContent>
      </Dialog>
  );
}

function AssignContractorDialog({ currentUser, contract, onChanged }) {
  const [open, setOpen] = useState(false);
  const [contractorId, setContractorId] = useState(contract.contractor?.client_id || "");
  const [submitting, setSubmitting] = useState(false);
  const [error, setError] = useState("");

  const contractors = PRESET_USERS.filter((user) => user.role === "CONTRACTOR");

  async function handleSubmit() {
    setSubmitting(true);
    setError("");
    try {
      await post("/customer/set_contractor", {
        customer_client_id: currentUser.client_id,
        contract_id: contract.id,
        contractor_client_id: contractorId,
      });
      setOpen(false);
      onChanged?.();
    } catch (e) {
      setError(e.message);
    } finally {
      setSubmitting(false);
    }
  }

  return (
      <Dialog open={open} onOpenChange={setOpen}>
        <DialogTrigger asChild>
          <Button variant="outline" size="sm">
            <UserPlus className="h-4 w-4 mr-2" />
            {contract.contractor ? "Сменить подрядчика" : "Назначить подрядчика"}
          </Button>
        </DialogTrigger>
        <DialogContent className="sm:max-w-lg max-w-[calc(100vw-2rem)] rounded-2xl overflow-hidden">
          <DialogHeader>
            <DialogTitle>Подрядчик для заказа #{contract.id}</DialogTitle>
          </DialogHeader>
          <div className="space-y-4 min-w-0">
            <div className="space-y-2">
              <Label>Подрядчик</Label>
              <Select value={contractorId} onValueChange={setContractorId}>
                <SelectTrigger>
                  <SelectValue placeholder="Выбери подрядчика" />
                </SelectTrigger>
                <SelectContent>
                  {contractors.map((contractor) => (
                      <SelectItem key={contractor.client_id} value={contractor.client_id}>
                        {contractor.name} {contractor.surname}
                      </SelectItem>
                  ))}
                </SelectContent>
              </Select>
            </div>

            <ErrorBox error={error} />

            <Button onClick={handleSubmit} disabled={submitting || !contractorId} className="w-full">
              Сохранить
            </Button>
          </div>
        </DialogContent>
      </Dialog>
  );
}

function ChangeContractStatusDialog({ currentUser, contract, onChanged }) {
  const [open, setOpen] = useState(false);
  const [status, setStatus] = useState(contract.status || "");
  const [submitting, setSubmitting] = useState(false);
  const [error, setError] = useState("");

  async function handleSubmit() {
    setSubmitting(true);
    setError("");
    try {
      await post("/contract/change_contract_status", {
        contract_status: status,
        client_id: currentUser.client_id,
        contract_id: contract.id,
      });
      setOpen(false);
      onChanged?.();
    } catch (e) {
      setError(e.message);
    } finally {
      setSubmitting(false);
    }
  }

  return (
      <Dialog open={open} onOpenChange={setOpen}>
        <DialogTrigger asChild>
          <Button variant="outline" size="sm">
            <PencilLine className="h-4 w-4 mr-2" /> Статус заказа
          </Button>
        </DialogTrigger>
        <DialogContent className="sm:max-w-lg max-w-[calc(100vw-2rem)] rounded-2xl overflow-hidden">
          <DialogHeader>
            <DialogTitle>Сменить статус заказа #{contract.id}</DialogTitle>
          </DialogHeader>

          <div className="space-y-4 min-w-0">
            <div className="rounded-lg border bg-slate-50 p-3 text-sm">
              <div><span className="font-medium">Текущий статус:</span> {contract.status}</div>
              <div className="mt-2 text-slate-500">
                Сервер сам проверит, разрешен ли переход.
              </div>
            </div>

            <div className="space-y-2">
              <Label>Новый статус</Label>
              <Select value={status} onValueChange={setStatus}>
                <SelectTrigger>
                  <SelectValue placeholder="Выбери статус" />
                </SelectTrigger>
                <SelectContent>
                  {CONTRACT_STATUSES.map((item) => (
                      <SelectItem key={item} value={item}>
                        {item}
                      </SelectItem>
                  ))}
                </SelectContent>
              </Select>
            </div>

            <ErrorBox error={error} />

            <Button onClick={handleSubmit} disabled={submitting || !status} className="w-full">
              Сохранить
            </Button>
          </div>
        </DialogContent>
      </Dialog>
  );
}

function ChangeXmlStatusDialog({ currentUser, contract, xmlVersion, onChanged }) {
  const [open, setOpen] = useState(false);
  const [status, setStatus] = useState(xmlVersion.status || "");
  const [reason, setReason] = useState(xmlVersion.reason || "");
  const [submitting, setSubmitting] = useState(false);
  const [error, setError] = useState("");

  async function handleSubmit() {
    setSubmitting(true);
    setError("");
    try {
      await post("/contract/xml/change_xml_status", {
        client_id: currentUser.client_id,
        xml_status: status,
        xml_name: xmlVersion.name,
        xsd_id: xmlVersion.xsd_light?.id,
        contract_id: contract.id,
        reason,
      });
      setOpen(false);
      onChanged?.();
    } catch (e) {
      setError(e.message);
    } finally {
      setSubmitting(false);
    }
  }

  return (
      <Dialog open={open} onOpenChange={setOpen}>
        <DialogTrigger asChild>
          <Button variant="outline" size="sm">
            <PencilLine className="h-4 w-4 mr-2" /> Статус XML
          </Button>
        </DialogTrigger>
        <DialogContent className="sm:max-w-lg max-w-[calc(100vw-2rem)] rounded-2xl overflow-hidden">
          <DialogHeader>
            <DialogTitle>Сменить статус XML</DialogTitle>
          </DialogHeader>
          <div className="space-y-4 min-w-0">
            <div className="rounded-lg border bg-slate-50 p-3 text-sm">
              <div><span className="font-medium">Документ:</span> {xmlVersion.name}</div>
              <div><span className="font-medium">Версия:</span> {xmlVersion.version}</div>
              <div><span className="font-medium">Текущий статус:</span> {xmlVersion.status}</div>
            </div>

            <div className="space-y-2">
              <Label>Новый статус</Label>
              <Select value={status} onValueChange={setStatus}>
                <SelectTrigger>
                  <SelectValue placeholder="Выбери статус" />
                </SelectTrigger>
                <SelectContent>
                  {XML_STATUSES.map((item) => (
                      <SelectItem key={item} value={item}>
                        {item}
                      </SelectItem>
                  ))}
                </SelectContent>
              </Select>
            </div>

            <div className="space-y-2">
              <Label>Причина / комментарий</Label>
              <Textarea value={reason} onChange={(e) => setReason(e.target.value)} placeholder="Особенно полезно для REFUSED" />
            </div>

            <ErrorBox error={error} />
            <Button onClick={handleSubmit} disabled={submitting || !status} className="w-full">
              Сохранить
            </Button>
          </div>
        </DialogContent>
      </Dialog>
  );
}

function AddXmlDialog({ currentUser, contract, onCreated }) {
  const [open, setOpen] = useState(false);
  const [xmlName, setXmlName] = useState("");
  const [selectedXsdId, setSelectedXsdId] = useState("");
  const [xsdOptions, setXsdOptions] = useState([]);
  const [loadingXsd, setLoadingXsd] = useState(false);
  const [submitting, setSubmitting] = useState(false);
  const [error, setError] = useState("");

  useEffect(() => {
    async function loadXsdList() {
      if (!open) return;
      setLoadingXsd(true);
      setError("");
      try {
        const data = await post("/get_xsd_light_list", {
          client_id: currentUser.client_id,
        });

        const list = data?.xsd_light_list || data?.xml_light_list || [];
        setXsdOptions(Array.isArray(list) ? list : []);
      } catch (e) {
        setError(e.message);
      } finally {
        setLoadingXsd(false);
      }
    }

    loadXsdList();
  }, [open, currentUser.client_id]);

  async function handleSubmit() {
    setSubmitting(true);
    setError("");
    try {
      await post("/contractor/contract/xml/add_new_xml_list", {
        client_id: currentUser.client_id,
        contract_id: contract.id,
        new_xml_data_list: [
          {
            xml_name: xmlName,
            xsd_id: Number(selectedXsdId),
          },
        ],
      });
      setXmlName("");
      setSelectedXsdId("");
      setOpen(false);
      onCreated?.();
    } catch (e) {
      setError(e.message);
    } finally {
      setSubmitting(false);
    }
  }

  return (
      <Dialog open={open} onOpenChange={setOpen}>
        <DialogTrigger asChild>
          <Button variant="outline" size="sm">
            <Plus className="h-4 w-4 mr-2" /> Добавить XML
          </Button>
        </DialogTrigger>
        <DialogContent className="sm:max-w-lg max-w-[calc(100vw-2rem)] rounded-2xl overflow-hidden">
          <DialogHeader>
            <DialogTitle>Добавить XML в заказ #{contract.id}</DialogTitle>
          </DialogHeader>
          <div className="space-y-4 min-w-0">
            <div className="space-y-2">
              <Label>Имя XML</Label>
              <Input value={xmlName} onChange={(e) => setXmlName(e.target.value)} placeholder="invoice.xml" />
            </div>

            <div className="space-y-2">
              <Label>XSD схема</Label>
              <Select value={selectedXsdId} onValueChange={setSelectedXsdId}>
                <SelectTrigger className="w-full max-w-full">
                  <SelectValue placeholder={loadingXsd ? "Загрузка XSD..." : "Выбери XSD"} />
                </SelectTrigger>
                <SelectContent className="max-w-[min(90vw,520px)]">
                  {xsdOptions.map((xsd) => (
                      <SelectItem key={String(xsd.id)} value={String(xsd.id)}>
                        {xsd.name} {xsd.stage ? `· stage: ${xsd.stage}` : ""}
                      </SelectItem>
                  ))}
                </SelectContent>
              </Select>
            </div>

            {selectedXsdId && (
                <Card className="rounded-xl border bg-slate-50/70 shadow-none w-full min-w-0 max-w-full overflow-hidden">
                  <CardContent className="p-4 min-w-0 max-w-full">
                    {(() => {
                      const selected = xsdOptions.find((x) => String(x.id) === String(selectedXsdId));
                      if (!selected) return null;
                      return (
                          <div className="grid grid-cols-1 sm:grid-cols-2 gap-3 text-sm min-w-0 max-w-full">
                            <Field label="XSD ID" value={selected.id} />
                            <Field label="Название" value={selected.name} />
                            <Field label="Stage" value={selected.stage} />
                            <Field label="Статус" value={selected.status} />
                            <Field label="Версия" value={selected.version} />
                            <Field label="Начало" value={selected.begin_date} />
                          </div>
                      );
                    })()}
                  </CardContent>
                </Card>
            )}

            <ErrorBox error={error} />

            <Button
                onClick={handleSubmit}
                disabled={submitting || loadingXsd || !xmlName.trim() || !selectedXsdId}
                className="w-full"
            >
              Сохранить
            </Button>
          </div>
        </DialogContent>
      </Dialog>
  );
}

function AddXmlVersionDialog({ currentUser, contract, xmlDoc, onCreated }) {
  const [open, setOpen] = useState(false);
  const [xmlData, setXmlData] = useState("");
  const [reason, setReason] = useState("");
  const [submitting, setSubmitting] = useState(false);
  const [error, setError] = useState("");

  async function handleSubmit() {
    setSubmitting(true);
    setError("");
    try {
      await post("/contractor/contract/xml/add_new_xml_version", {
        xml_name: xmlDoc.xml_name,
        xsd_id: Number(xmlDoc.xsd_id),
        contract_id: contract.id,
        client_id: currentUser.client_id,
        xml_data: xmlData,
        reason,
      });
      setXmlData("");
      setReason("");
      setOpen(false);
      onCreated?.();
    } catch (e) {
      setError(e.message);
    } finally {
      setSubmitting(false);
    }
  }

  return (
      <Dialog open={open} onOpenChange={setOpen}>
        <DialogTrigger asChild>
          <Button variant="outline" size="sm">
            <Upload className="h-4 w-4 mr-2" /> Новая версия
          </Button>
        </DialogTrigger>
        <DialogContent className="max-w-3xl max-w-[calc(100vw-2rem)] rounded-2xl overflow-hidden">
          <DialogHeader>
            <DialogTitle>Добавить новую версию: {xmlDoc.xml_name}</DialogTitle>
          </DialogHeader>
          <div className="space-y-4 min-w-0">
            <Card className="rounded-xl border bg-slate-50/70 shadow-none overflow-hidden min-w-0">
              <CardContent className="p-4 grid grid-cols-1 sm:grid-cols-2 gap-3 text-sm min-w-0">
                <Field label="XSD" value={xmlDoc.xsd_name} />
                <Field label="Stage" value={xmlDoc.xsd_stage} />
              </CardContent>
            </Card>

            <div className="space-y-2">
              <Label>XML data</Label>
              <Textarea
                  value={xmlData}
                  onChange={(e) => setXmlData(e.target.value)}
                  className="min-h-[280px] max-h-[60vh] w-full max-w-full resize-y overflow-auto font-mono text-xs break-all"
                  placeholder="<root>...</root>"
              />
            </div>
            <div className="space-y-2">
              <Label>Комментарий / причина</Label>
              <Textarea value={reason} onChange={(e) => setReason(e.target.value)} placeholder="Что изменилось в версии" />
            </div>

            <ErrorBox error={error} />

            <Button onClick={handleSubmit} disabled={submitting || !xmlData.trim()} className="w-full">
              Отправить новую версию
            </Button>
          </div>
        </DialogContent>
      </Dialog>
  );
}

function XmlVersionViewer({ currentUser, selectedVersion, onReload }) {
  const [xmlData, setXmlData] = useState("");
  const [formattedXml, setFormattedXml] = useState("");
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState("");

  useEffect(() => {
    async function load() {
      if (!selectedVersion?.id || !currentUser?.client_id) {
        setXmlData("");
        setFormattedXml("");
        return;
      }

      setLoading(true);
      setError("");
      try {
        const data = await post("/contract/get_xml_data", {
          client_id: currentUser.client_id,
          xml_id: selectedVersion.id,
        });

        const rawXml = data.xml_data || "";
        setXmlData(rawXml);
        setFormattedXml(formatXml(rawXml));
      } catch (e) {
        setError(e.message);
      } finally {
        setLoading(false);
      }
    }

    load();
  }, [selectedVersion?.id, currentUser?.client_id]);

  if (!selectedVersion) {
    return (
        <Card className="rounded-2xl h-full min-w-0">
          <CardHeader>
            <CardTitle>XML содержимое</CardTitle>
            <CardDescription>Выбери версию документа слева</CardDescription>
          </CardHeader>
        </Card>
    );
  }

  return (
      <Card className="rounded-2xl h-full min-w-0">
        <CardHeader className="space-y-3">
          <div className="flex flex-col gap-3 xl:flex-row xl:items-start xl:justify-between">
            <div>
              <CardTitle>{selectedVersion.name}</CardTitle>
              <CardDescription>
                Версия {selectedVersion.version} · статус {selectedVersion.status}
              </CardDescription>
            </div>
            <div className="flex flex-wrap gap-2">
              <ChangeXmlStatusDialog
                  currentUser={currentUser}
                  contract={selectedVersion.contract}
                  xmlVersion={selectedVersion}
                  onChanged={onReload}
              />
            </div>
          </div>
        </CardHeader>

        <CardContent className="space-y-4 min-w-0">
          <div className="grid md:grid-cols-3 gap-4 min-w-0">
            <Field label="Контракт" value={selectedVersion.contract?.name} />
            <Field label="XML ID" value={selectedVersion.id} />
            <Field label="Причина" value={selectedVersion.reason} />
          </div>

          <Card className="rounded-xl border bg-slate-50/70 shadow-none min-w-0 overflow-hidden">
            <CardHeader className="pb-3">
              <CardTitle className="text-base">Информация по XSD</CardTitle>
            </CardHeader>
            <CardContent className="min-w-0">
              <div className="grid md:grid-cols-2 xl:grid-cols-3 gap-4 min-w-0">
                <Field label="XSD ID" value={selectedVersion.xsd_light?.id} />
                <Field label="Название" value={selectedVersion.xsd_light?.name} />
                <Field label="Стадия" value={selectedVersion.xsd_light?.stage} />
                <Field label="Статус" value={selectedVersion.xsd_light?.status} />
                <Field label="Версия" value={selectedVersion.xsd_light?.version} />
                <Field label="Дата начала" value={selectedVersion.xsd_light?.begin_date} />
                <Field label="Дата окончания" value={selectedVersion.xsd_light?.end_date} />
                <Field label="Ссылка" value={selectedVersion.xsd_light?.link} />
              </div>
            </CardContent>
          </Card>

          {loading && <div className="text-sm text-slate-500">Загрузка XML...</div>}
          <ErrorBox error={error} />

          {!loading && !error && (
              <ScrollArea className="h-[520px] w-full max-w-full rounded-xl border bg-slate-50">
                <div className="p-4 overflow-x-auto">
              <pre className="text-xs whitespace-pre font-mono min-w-0 max-w-full">
                {formattedXml || xmlData || "Пусто"}
              </pre>
                </div>
              </ScrollArea>
          )}
        </CardContent>
      </Card>
  );
}

function XmlDiffViewer({ currentUser, compareVersions, onClear }) {
  const [leftXml, setLeftXml] = useState("");
  const [rightXml, setRightXml] = useState("");
  const [formattedLeftXml, setFormattedLeftXml] = useState("");
  const [formattedRightXml, setFormattedRightXml] = useState("");
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState("");

  useEffect(() => {
    async function load() {
      if (!compareVersions?.left?.id || !compareVersions?.right?.id || !currentUser?.client_id) {
        setLeftXml("");
        setRightXml("");
        setFormattedLeftXml("");
        setFormattedRightXml("");
        return;
      }

      setLoading(true);
      setError("");
      try {
        const [leftData, rightData] = await Promise.all([
          post("/contract/get_xml_data", {
            client_id: currentUser.client_id,
            xml_id: compareVersions.left.id,
          }),
          post("/contract/get_xml_data", {
            client_id: currentUser.client_id,
            xml_id: compareVersions.right.id,
          }),
        ]);

        const leftRaw = leftData.xml_data || "";
        const rightRaw = rightData.xml_data || "";

        setLeftXml(leftRaw);
        setRightXml(rightRaw);
        setFormattedLeftXml(formatXml(leftRaw));
        setFormattedRightXml(formatXml(rightRaw));
      } catch (e) {
        setError(e.message);
      } finally {
        setLoading(false);
      }
    }

    load();
  }, [compareVersions?.left?.id, compareVersions?.right?.id, currentUser?.client_id]);

  const rows = useMemo(
      () => buildDiffRows(formattedLeftXml || leftXml, formattedRightXml || rightXml),
      [formattedLeftXml, formattedRightXml, leftXml, rightXml]
  );

  if (!compareVersions?.left || !compareVersions?.right) {
    return (
        <Card className="rounded-2xl h-full min-w-0">
          <CardHeader>
            <CardTitle>Сравнение версий</CardTitle>
            <CardDescription>Выбери две версии одного XML в списке слева</CardDescription>
          </CardHeader>
        </Card>
    );
  }

  return (
      <Card className="rounded-2xl h-full min-w-0">
        <CardHeader>
          <div className="flex items-start justify-between gap-4">
            <div>
              <CardTitle>Diff версий</CardTitle>
              <CardDescription>
                {compareVersions.left.name} · v{compareVersions.left.version} → v{compareVersions.right.version}
              </CardDescription>
            </div>
            <Button variant="outline" size="sm" onClick={onClear}>Сбросить сравнение</Button>
          </div>
        </CardHeader>
        <CardContent className="space-y-4">
          <div className="grid md:grid-cols-2 gap-4">
            <Card className="rounded-xl border shadow-none bg-slate-50/70">
              <CardContent className="p-4">
                <div className="text-xs text-slate-500 mb-1">Старая версия</div>
                <div className="flex items-center justify-between gap-2">
                  <div className="text-sm font-medium">
                    id={compareVersions.left.id}, v{compareVersions.left.version}
                  </div>
                  <StatusBadge value={compareVersions.left.status} />
                </div>
              </CardContent>
            </Card>

            <Card className="rounded-xl border shadow-none bg-slate-50/70">
              <CardContent className="p-4">
                <div className="text-xs text-slate-500 mb-1">Новая версия</div>
                <div className="flex items-center justify-between gap-2">
                  <div className="text-sm font-medium">
                    id={compareVersions.right.id}, v{compareVersions.right.version}
                  </div>
                  <StatusBadge value={compareVersions.right.status} />
                </div>
              </CardContent>
            </Card>
          </div>

          {loading && <div className="text-sm text-slate-500">Загрузка двух версий...</div>}
          <ErrorBox error={error} />

          {!loading && !error && (
              <ScrollArea className="h-[620px] w-full max-w-full rounded-xl border bg-white">
                <div className="font-mono text-xs">
                  {rows.map((row, idx) => (
                      <div
                          key={row.id}
                          className={`grid grid-cols-[56px,28px,1fr] border-b ${
                              row.type === "added"
                                  ? "bg-emerald-50"
                                  : row.type === "removed"
                                      ? "bg-red-50"
                                      : "bg-white"
                          }`}
                      >
                        <div className="px-3 py-1 text-right text-slate-400 select-none border-r">
                          {idx + 1}
                        </div>
                        <div className="px-2 py-1 text-center text-slate-500 border-r select-none">
                          {row.type === "added" ? "+" : row.type === "removed" ? "-" : " "}
                        </div>
                        <div className="px-3 py-1 whitespace-pre-wrap break-all">
                          {row.text}
                        </div>
                      </div>
                  ))}
                </div>
              </ScrollArea>
          )}
        </CardContent>
      </Card>
  );
}

function XmlDocumentAccordion({
                                xmlDoc,
                                currentUser,
                                contract,
                                onReload,
                                onSelectVersion,
                                selectedVersionId,
                                compareSelection,
                                onToggleCompare,
                              }) {
  const [isOpen, setIsOpen] = useState(false);

  const inheritedDocStatus = xmlDoc.versions?.[0]?.status || contract.status;
  const docBarClass = getStatusAppearance(inheritedDocStatus).bar;

  return (
      <div className="relative rounded-xl border bg-slate-50/60 overflow-hidden">
        <div className={`absolute left-0 top-0 h-full w-1 ${docBarClass}`} />

        <div className="p-3 pl-4">
          <button
              type="button"
              onClick={() => setIsOpen((prev) => !prev)}
              className="w-full flex items-center justify-between gap-3 text-left"
          >
            <div className="min-w-0">
              <div className="text-sm font-semibold flex items-center gap-2">
                {isOpen ? <ChevronDown className="h-4 w-4 shrink-0" /> : <ChevronRight className="h-4 w-4 shrink-0" />}
                <FileText className="h-4 w-4 shrink-0" />
                <span className="break-all">{xmlDoc.xml_name}</span>
              </div>
              <div className="text-xs text-slate-500 mt-1 break-all">
                XSD: {xmlDoc.xsd_name || xmlDoc.xsd_id}
                {xmlDoc.xsd_stage ? ` · stage: ${xmlDoc.xsd_stage}` : ""}
                {" · "}Версий: {xmlDoc.versions.length}
              </div>
            </div>

            <div className="flex items-center gap-2 shrink-0">
              <StatusBadge value={xmlDoc.versions?.[0]?.status} />
              {currentUser.role === "CONTRACTOR" && (
                  <div onClick={(e) => e.stopPropagation()}>
                    <AddXmlVersionDialog
                        currentUser={currentUser}
                        contract={contract}
                        xmlDoc={xmlDoc}
                        onCreated={onReload}
                    />
                  </div>
              )}
            </div>
          </button>

          {isOpen && (
              <div className="space-y-2 mt-3">
                {xmlDoc.versions.map((version) => {
                  const versionBarClass = getStatusAppearance(version.status || inheritedDocStatus).bar;

                  return (
                      <div key={version.id} className="relative rounded-lg border bg-white overflow-hidden">
                        <div className={`absolute left-0 top-0 h-full w-1 ${versionBarClass}`} />

                        <button
                            onClick={() => onSelectVersion(version)}
                            className={`w-full text-left px-3 py-2 pl-4 transition ${
                                selectedVersionId === version.id ? "bg-slate-900 text-white" : "bg-white hover:bg-slate-100"
                            }`}
                        >
                          <div className="flex items-center justify-between gap-2">
                            <div className="text-sm font-medium">Версия {version.version}</div>
                            <StatusBadge value={version.status} />
                          </div>
                          <div className={`text-xs mt-1 ${selectedVersionId === version.id ? "text-slate-300" : "text-slate-500"}`}>
                            id={version.id} {version.reason ? `· ${version.reason}` : ""}
                          </div>
                        </button>

                        <div className="flex flex-wrap items-center justify-between gap-2 px-3 py-2 pl-4 border-t bg-slate-50">
                          <label className="flex items-center gap-2 text-xs text-slate-600 cursor-pointer">
                            <input
                                type="checkbox"
                                checked={compareSelection.includes(version.id)}
                                onChange={() => onToggleCompare(version, xmlDoc)}
                            />
                            Выбрать для сравнения
                          </label>

                          <ChangeXmlStatusDialog
                              currentUser={currentUser}
                              contract={contract}
                              xmlVersion={version}
                              onChanged={onReload}
                          />
                        </div>
                      </div>
                  );
                })}
              </div>
          )}
        </div>
      </div>
  );
}

function ContractsSidebar({
                            contracts,
                            currentUser,
                            onReload,
                            onSelectVersion,
                            selectedVersionId,
                            compareSelection,
                            onToggleCompare,
                          }) {
  return (
      <ScrollArea className="h-[calc(100vh-220px)] pr-2">
        <div className="space-y-4">
          {contracts.map((contract) => {
            const contractBarClass = getStatusAppearance(contract.status).bar;

            return (
                <Card key={contract.id} className="relative rounded-2xl overflow-hidden">
                  <div className={`absolute left-0 top-0 h-full w-1.5 ${contractBarClass}`} />

                  <CardHeader className="pb-3 pl-5">
                    <div className="flex items-start justify-between gap-3">
                      <div className="min-w-0">
                        <CardTitle className="text-base break-all">#{contract.id} · {contract.name}</CardTitle>
                        <CardDescription className="mt-1 break-all">
                          {contract.description || "Без описания"}
                        </CardDescription>
                      </div>
                      <StatusBadge value={contract.status} />
                    </div>
                  </CardHeader>

                  <CardContent className="space-y-3 pl-5">
                    <div className="grid grid-cols-2 gap-3 text-sm">
                      <Field label="Заказчик" value={`${contract.customer?.name || ""} ${contract.customer?.surname || ""}`.trim()} />
                      <Field
                          label="Подрядчик"
                          value={
                            contract.contractor
                                ? `${contract.contractor?.name || ""} ${contract.contractor?.surname || ""}`.trim()
                                : "Не назначен"
                          }
                      />
                    </div>

                    <div className="flex flex-wrap gap-2">
                      {currentUser.role === "CUSTOMER" && (
                          <>
                            <ChangeContractStatusDialog
                                currentUser={currentUser}
                                contract={contract}
                                onChanged={onReload}
                            />
                            {(!contract.contractor || !contract.contractor.client_id) && contract.status === "NEW" && (
                                <AssignContractorDialog
                                    currentUser={currentUser}
                                    contract={contract}
                                    onChanged={onReload}
                                />
                            )}
                          </>
                      )}

                      {currentUser.role === "CONTRACTOR" && (
                          <AddXmlDialog currentUser={currentUser} contract={contract} onCreated={onReload} />
                      )}
                    </div>

                    <Separator />

                    {contract.xmlDocuments?.length ? (
                        <div className="space-y-3">
                          {contract.xmlDocuments.map((xmlDoc) => (
                              <XmlDocumentAccordion
                                  key={`${xmlDoc.xml_name}-${xmlDoc.xsd_id}`}
                                  xmlDoc={xmlDoc}
                                  currentUser={currentUser}
                                  contract={contract}
                                  onReload={onReload}
                                  onSelectVersion={onSelectVersion}
                                  selectedVersionId={selectedVersionId}
                                  compareSelection={compareSelection}
                                  onToggleCompare={onToggleCompare}
                              />
                          ))}
                        </div>
                    ) : (
                        <div className="text-sm text-slate-500">XML документов пока нет</div>
                    )}
                  </CardContent>
                </Card>
            );
          })}
        </div>
      </ScrollArea>
  );
}

function Dashboard({ currentUser, onLogout }) {
  const [contracts, setContracts] = useState([]);
  const [selectedVersion, setSelectedVersion] = useState(null);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState("");

  const [compareSelection, setCompareSelection] = useState([]);
  const [compareVersions, setCompareVersions] = useState({ left: null, right: null, groupKey: null });

  async function loadAll() {
    setLoading(true);
    setError("");
    try {
      const [xmlResponse, contractsResponse] = await Promise.all([
        post("/contract/get_xml_light_info_list", { client_id: currentUser.client_id }).catch(() => ({ xml_light_list: [] })),
        post("/contract/get_contracts", { client_id: currentUser.client_id }).catch(() => ({ contract_list: [] })),
      ]);

      const merged = groupByContract(xmlResponse.xml_light_list || [], contractsResponse.contract_list || []);
      setContracts(merged);

      if (selectedVersion?.id) {
        const updatedSelection = merged
            .flatMap((contract) => contract.xmlDocuments || [])
            .flatMap((doc) => doc.versions || [])
            .find((version) => version.id === selectedVersion.id);
        setSelectedVersion(updatedSelection || null);
      }

      if (compareVersions.left?.id || compareVersions.right?.id) {
        const allVersions = merged
            .flatMap((contract) => contract.xmlDocuments || [])
            .flatMap((doc) => doc.versions || []);

        const left = allVersions.find((v) => v.id === compareVersions.left?.id) || null;
        const right = allVersions.find((v) => v.id === compareVersions.right?.id) || null;

        setCompareVersions((prev) => ({ ...prev, left, right }));
      }
    } catch (e) {
      setError(e.message);
    } finally {
      setLoading(false);
    }
  }

  useEffect(() => {
    loadAll();
  }, [currentUser.client_id]);

  const stats = useMemo(() => {
    const xmlDocs = contracts.flatMap((c) => c.xmlDocuments || []);
    const versions = xmlDocs.flatMap((d) => d.versions || []);
    return {
      contracts: contracts.length,
      xmlDocs: xmlDocs.length,
      versions: versions.length,
    };
  }, [contracts]);

  function handleToggleCompare(version, xmlDoc) {
    const groupKey = `${xmlDoc.xml_name}__${xmlDoc.xsd_id}`;

    setCompareVersions((prev) => {
      const currentIds = [prev.left?.id, prev.right?.id].filter(Boolean);
      let nextIds = currentIds.includes(version.id)
          ? currentIds.filter((id) => id !== version.id)
          : [...currentIds, version.id].slice(-2);

      const sameGroup = !prev.groupKey || prev.groupKey === groupKey;
      if (!sameGroup && !currentIds.includes(version.id)) {
        nextIds = [version.id];
      }

      const allVersions = contracts
          .flatMap((contract) => contract.xmlDocuments || [])
          .flatMap((doc) => doc.versions || []);

      return {
        groupKey: nextIds.length ? groupKey : null,
        left: allVersions.find((v) => v.id === nextIds[0]) || null,
        right: allVersions.find((v) => v.id === nextIds[1]) || null,
      };
    });
  }

  useEffect(() => {
    const ids = [compareVersions.left?.id, compareVersions.right?.id].filter(Boolean);
    setCompareSelection(ids);
  }, [compareVersions.left?.id, compareVersions.right?.id]);

  return (
      <div className="min-h-screen bg-slate-50 p-4 md:p-8">
        <div className="max-w-[1600px] mx-auto space-y-6">
          <AppHeader currentUser={currentUser} onLogout={onLogout} onRefresh={loadAll} loading={loading} />

          <div className="grid md:grid-cols-3 gap-4">
            <Card className="rounded-2xl">
              <CardContent className="p-5">
                <div className="text-sm text-slate-500">Заказы</div>
                <div className="text-3xl font-semibold mt-1">{stats.contracts}</div>
              </CardContent>
            </Card>
            <Card className="rounded-2xl">
              <CardContent className="p-5">
                <div className="text-sm text-slate-500">XML документы</div>
                <div className="text-3xl font-semibold mt-1">{stats.xmlDocs}</div>
              </CardContent>
            </Card>
            <Card className="rounded-2xl">
              <CardContent className="p-5">
                <div className="text-sm text-slate-500">Версии</div>
                <div className="text-3xl font-semibold mt-1">{stats.versions}</div>
              </CardContent>
            </Card>
          </div>

          <Tabs defaultValue="contracts" className="space-y-4">
            <div className="flex flex-col md:flex-row md:items-center md:justify-between gap-4">
              <TabsList>
                <TabsTrigger value="contracts"><FolderKanban className="h-4 w-4 mr-2" />Заказы</TabsTrigger>
                <TabsTrigger value="viewer"><FileText className="h-4 w-4 mr-2" />Просмотр XML</TabsTrigger>
                <TabsTrigger value="compare"><FileText className="h-4 w-4 mr-2" />Сравнение версий</TabsTrigger>
              </TabsList>

              {currentUser.role === "CUSTOMER" && (
                  <CreateContractDialog currentUser={currentUser} onCreated={loadAll} />
              )}
            </div>

            <ErrorBox error={error} />

            <TabsContent value="contracts">
              <div className="grid xl:grid-cols-[560px,minmax(0,1fr)] gap-6">
                <ContractsSidebar
                    contracts={contracts}
                    currentUser={currentUser}
                    onReload={loadAll}
                    onSelectVersion={setSelectedVersion}
                    selectedVersionId={selectedVersion?.id}
                    compareSelection={compareSelection}
                    onToggleCompare={handleToggleCompare}
                />
                <XmlVersionViewer
                    currentUser={currentUser}
                    selectedVersion={selectedVersion}
                    onReload={loadAll}
                />
              </div>
            </TabsContent>

            <TabsContent value="viewer">
              <XmlVersionViewer
                  currentUser={currentUser}
                  selectedVersion={selectedVersion}
                  onReload={loadAll}
              />
            </TabsContent>

            <TabsContent value="compare">
              <XmlDiffViewer
                  currentUser={currentUser}
                  compareVersions={compareVersions}
                  onClear={() => {
                    setCompareVersions({ left: null, right: null, groupKey: null });
                    setCompareSelection([]);
                  }}
              />
            </TabsContent>
          </Tabs>
        </div>
      </div>
  );
}

export default function XmlContractsFrontendMvp() {
  const [currentUser, setCurrentUser] = useState(null);

  if (!currentUser) {
    return <LoginScreen onSelect={setCurrentUser} />;
  }

  return <Dashboard currentUser={currentUser} onLogout={() => setCurrentUser(null)} />;
}