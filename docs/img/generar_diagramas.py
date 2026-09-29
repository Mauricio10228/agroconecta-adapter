import subprocess, html
FONT="Liberation Sans"
def cls(name, stereo=None, attrs=(), meths=(), fill="#FFFFFF", border="#333333", italic=False, dashed=False):
    esc=html.escape
    rows=[]
    if stereo: rows.append(f'<TR><TD ALIGN="CENTER"><FONT POINT-SIZE="10">«{esc(stereo)}»</FONT></TD></TR>')
    nm = f'<I>{esc(name)}</I>' if italic else esc(name)
    rows.append(f'<TR><TD ALIGN="CENTER"><B>{nm}</B></TD></TR>')
    a="<BR ALIGN=\"LEFT\"/>".join(esc(x) for x in attrs) + ('<BR ALIGN="LEFT"/>' if attrs else '')
    m="<BR ALIGN=\"LEFT\"/>".join(esc(x) for x in meths) + ('<BR ALIGN="LEFT"/>' if meths else '')
    head=f'<TABLE BORDER="0" CELLBORDER="1" CELLSPACING="0" CELLPADDING="5" BGCOLOR="{fill}" COLOR="{border}">'
    body=''.join(rows)
    body+=f'<TR><TD ALIGN="LEFT"><FONT POINT-SIZE="10">{a if a else " "}</FONT></TD></TR>'
    body+=f'<TR><TD ALIGN="LEFT"><FONT POINT-SIZE="10">{m if m else " "}</FONT></TD></TR>'
    return f'label=<{head}{body}</TABLE>>, shape=plaintext'
def render(dot, out, dpi=200):
    open(out+'.dot','w',encoding='utf-8').write(dot)
    subprocess.run(['dot','-Tpng',f'-Gdpi={dpi}',out+'.dot','-o',out+'.png'],check=True)


RUTA_LINE = '   ruta = origen + "-" + destino'
# ---------- Figura 1: mapa de dependencias ANTES ----------
d1=f'''digraph G {{
 graph [rankdir=TB, nodesep=0.5, ranksep=0.7, fontname="{FONT}", pad=0.15];
 node [fontname="{FONT}"]; edge [fontname="{FONT}", fontsize=10];
 A [{cls("LogisticaServiceAjustado", attrs=["- proveedorLocal : ServicioEnvio","- rapidExpress : RapidExpressAPI"], meths=["+ cotizar(proveedor : String,","    origen : String, destino : String,","    pesoKg : double) : double"], fill="#FDECEA", border="#B71C1C")}];
 S [{cls("ServicioEnvio", stereo="interface", meths=["+ calcularCosto(origen : String,","    destino : String,","    peso : double) : double"], italic=True, fill="#EEF3F8")}];
 R [{cls("RapidExpressAPI", stereo="librería externa (no modificable)", meths=["+ getShippingPrice(route : String,","    weightInGrams : int) : double"], fill="#F5F5F5")}];
 A -> S [arrowhead=vee, label="  usa (abstracción)"];
 A -> R [arrowhead=vee, color="#B71C1C", fontcolor="#B71C1C", penwidth=2, label="  depende de clase concreta\\l  (formato y unidades propios)\\l"];
}}'''
render(d1,'fig1_dependencias_antes')

# ---------- Figura 2: UML Adapter adaptado ----------
d2=f'''digraph G {{
 graph [rankdir=TB, nodesep=0.25, ranksep=0.6, fontname="{FONT}", pad=0.15, newrank=true];
 node [fontname="{FONT}"]; edge [fontname="{FONT}", fontsize=10];
 CFG [{cls("ConfiguracionLogistica", stereo="raíz de composición", meths=["+ crear(proveedorLocal :","    ServicioEnvio) : LogisticaService"], fill="#F3E5F5", border="#6A1B9A")}];
 C [{cls("LogisticaService", stereo="Client", attrs=["- proveedores :","    Map<String, ServicioEnvio>"], meths=["+ cotizar(proveedor, origen,","    destino, pesoKg) : double"], fill="#E8F5E9", border="#1B5E20")}];
 T [{cls("ServicioEnvio", stereo="Target (interface)", meths=["+ calcularCosto(origen : String,","    destino : String,","    peso : double) : double"], italic=True, fill="#E3F2FD", border="#0D47A1")}];
 L [{cls("Proveedor local", stereo="implementación existente", meths=["+ calcularCosto(origen,","    destino, peso)"], fill="#FFFFFF", border="#555555")}];
 AR [{cls("RapidExpressAdapter", stereo="Adapter", attrs=["- api : RapidExpressAPI"], meths=["+ calcularCosto(origen,","    destino, peso)","  ruta = origen + \"-\" + destino","  gramos = round(peso * 1000)"], fill="#FFF3E0", border="#E65100")}];
 AA [{cls("AndinaCargoAdapter", stereo="Adapter (nuevo requisito)", attrs=["- api : AndinaCargoAPI"], meths=["+ calcularCosto(origen,","    destino, peso)","  libras = peso * 2.20462"], fill="#FFF3E0", border="#E65100")}];
 XR [{cls("RapidExpressAPI", stereo="Adaptee", meths=["+ getShippingPrice(route : String,","    weightInGrams : int) : double"], fill="#F5F5F5")}];
 XA [{cls("AndinaCargoAPI", stereo="Adaptee", meths=["+ quote(originCity,","    destinationCity : String,","    weightInPounds : double) : double"], fill="#F5F5F5")}];
 C -> T [arrowhead=vee, headlabel="1..*  ", label="  usa"];
 T -> L [dir=back, arrowtail=empty, style=dashed];
 T -> AR [dir=back, arrowtail=empty, style=dashed];
 T -> AA [dir=back, arrowtail=empty, style=dashed];
 AR -> XR [arrowhead=vee, label=" adapta"];
 AA -> XA [arrowhead=vee, label=" adapta"];
 CFG -> C [arrowhead=vee, style=dashed, color="#6A1B9A", fontcolor="#6A1B9A", label="«create»"];
 {{rank=same; CFG; C}}
 {{rank=same; L; AR; AA}}
 {{rank=same; XR; XA}}
}}'''
render(d2,'fig2_uml_adapter')

# ---------- Figura 3: ubicación arquitectónica (puertos y adaptadores) ----------
d3=f'''digraph G {{
 graph [rankdir=TB, nodesep=0.25, ranksep=0.5, fontname="{FONT}", pad=0.15, newrank=true];
 node [fontname="{FONT}", shape=box, style="rounded,filled", fillcolor="#FFFFFF", fontsize=12]; edge [fontname="{FONT}", fontsize=10];
 subgraph cluster_in {{ label="Capa de aplicación (ilustrativa)"; style="rounded,filled"; fillcolor="#F3E5F5"; color="#6A1B9A"; fontsize=12;
   UC [label="Servicio de pedidos (caso de uso: cotizar envío)"]; }}
 subgraph cluster_core {{ label="Núcleo: lógica de logística"; style="rounded,filled"; fillcolor="#E8F5E9"; color="#1B5E20"; fontsize=12;
   LS [label="LogisticaService (Client)"]; P [label="«puerto de salida»  ServicioEnvio (Target)", fillcolor="#E3F2FD", color="#0D47A1"]; }}
 subgraph cluster_ad {{ label="Adaptadores de infraestructura (patrón Adapter)"; style="rounded,filled"; fillcolor="#FFF3E0"; color="#E65100"; fontsize=12;
   AL [label="Proveedor local\\n(ya conforme)"]; AR [label="Rapid-\\nExpressAdapter"]; AA [label="AndinaCargo-\\nAdapter"]; AN [label="Adapter N\\n(futuros)", style="rounded,dashed,filled"]; }}
 subgraph cluster_ext {{ label="Sistemas externos (no modificables)"; style="rounded,filled"; fillcolor="#F5F5F5"; color="#555555"; fontsize=12;
   EL [label="API proveedor\\nlocal"]; ER [label="Rapid-\\nExpressAPI"]; EA [label="AndinaCargo-\\nAPI"]; EN [label="API operador\\nN", style="rounded,dashed,filled"]; }}
 UC -> LS [label=" invoca"];
 LS -> P [label=" depende de la abstracción"];
 P -> AL [dir=back, arrowtail=empty, style=dashed]; P -> AR [dir=back, arrowtail=empty, style=dashed]; P -> AA [dir=back, arrowtail=empty, style=dashed]; P -> AN [dir=back, arrowtail=empty, style=dashed];
 AL -> EL; AR -> ER; AA -> EA; AN -> EN [style=dashed];
}}'''
render(d3,'fig3_arquitectura')
