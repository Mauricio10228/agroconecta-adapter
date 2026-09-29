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

# ---------- Figura 1: mapa de dependencias ANTES ----------
d1=f'''digraph G {{
 graph [rankdir=TB, nodesep=0.7, ranksep=0.8, fontname="{FONT}", pad=0.2];
 node [fontname="{FONT}"]; edge [fontname="{FONT}", fontsize=10];
 A [{cls("LogisticaServiceAjustado", attrs=["- proveedorLocal : ServicioEnvio","- rapidExpress : RapidExpressAPI"], meths=["+ cotizar(proveedor : String, origen : String,","    destino : String, pesoKg : double) : double"], fill="#FDECEA", border="#B71C1C")}];
 S [{cls("ServicioEnvio", stereo="interface", meths=["+ calcularCosto(origen, destino, peso : double) : double"], italic=True, fill="#EEF3F8")}];
 R [{cls("RapidExpressAPI", stereo="librería externa (no modificable)", meths=["+ getShippingPrice(route : String,","    weightInGrams : int) : double"], fill="#F5F5F5")}];
 A -> S [arrowhead=vee, label="  usa (abstracción)"];
 A -> R [arrowhead=vee, color="#B71C1C", fontcolor="#B71C1C", penwidth=2, label="  depende de clase concreta\\l  (unidades y formato propios)\\l"];
 N [shape=note, style=filled, fillcolor="#FFF8E1", color="#8D6E00", fontsize=10, label="Cambio futuro: 3 operadores más\\l→ 3 campos nuevos + 3 bloques if\\l   + 3 conversiones dentro de cotizar()\\l"];
 A -> N [style=dotted, arrowhead=none, color="#8D6E00"];
}}'''
render(d1,'fig1_dependencias_antes')

RUTA_LINE = '   ruta = origen + "-" + destino'
# ---------- Figura 2: UML Adapter adaptado ----------
d2=f'''digraph G {{
 graph [rankdir=TB, nodesep=0.35, ranksep=0.7, fontname="{FONT}", pad=0.2, newrank=true];
 node [fontname="{FONT}"]; edge [fontname="{FONT}", fontsize=10];
 CFG [{cls("ConfiguracionLogistica", stereo="raíz de composición", meths=["+ crear(proveedorLocal : ServicioEnvio) : LogisticaService"], fill="#F3E5F5", border="#6A1B9A")}];
 C [{cls("LogisticaService", stereo="Client", attrs=["- proveedores : Map<String, ServicioEnvio>"], meths=["+ cotizar(proveedor, origen, destino,","    pesoKg : double) : double"], fill="#E8F5E9", border="#1B5E20")}];
 T [{cls("ServicioEnvio", stereo="Target (interface)", meths=["+ calcularCosto(origen : String, destino : String,","    peso : double) : double"], italic=True, fill="#E3F2FD", border="#0D47A1")}];
 L [{cls("Proveedor local", stereo="implementación existente", meths=["+ calcularCosto(origen, destino, peso)"], fill="#FFFFFF", border="#555555")}];
 AR [{cls("RapidExpressAdapter", stereo="Adapter", attrs=["- api : RapidExpressAPI"], meths=["+ calcularCosto(origen, destino, peso)",RUTA_LINE,"   gramos = round(peso * 1000)"], fill="#FFF3E0", border="#E65100")}];
 AA [{cls("AndinaCargoAdapter", stereo="Adapter (nuevo requisito)", attrs=["- api : AndinaCargoAPI"], meths=["+ calcularCosto(origen, destino, peso)","   libras = peso * 2.20462"], fill="#FFF3E0", border="#E65100")}];
 XR [{cls("RapidExpressAPI", stereo="Adaptee", meths=["+ getShippingPrice(route : String,","    weightInGrams : int) : double"], fill="#F5F5F5")}];
 XA [{cls("AndinaCargoAPI", stereo="Adaptee", meths=["+ quote(originCity, destinationCity : String,","    weightInPounds : double) : double"], fill="#F5F5F5")}];
 C -> T [arrowhead=vee, headlabel="1..*  ", label="  usa"];
 T -> L [dir=back, arrowtail=empty, style=dashed];
 T -> AR [dir=back, arrowtail=empty, style=dashed];
 T -> AA [dir=back, arrowtail=empty, style=dashed];
 AR -> XR [arrowhead=vee, label=" adapta"];
 AA -> XA [arrowhead=vee, label=" adapta"];
 CFG -> C [arrowhead=vee, style=dashed, color="#6A1B9A", fontcolor="#6A1B9A", label="«create»"];
 CN [shape=note, style=filled, fillcolor="#F3E5F5", color="#6A1B9A", fontsize=10, label="Registra LOCAL, RAPID y ANDINA\\len el Map que recibe LogisticaService\\l"];
 CFG -> CN [style=dotted, arrowhead=none, color="#6A1B9A"];
 {{rank=same; CFG; C}}
 {{rank=same; L; AR; AA}}
 {{rank=same; XR; XA}}
}}'''
render(d2,'fig2_uml_adapter')

# ---------- Figura 3: ubicación arquitectónica (puertos y adaptadores) ----------
d3=f'''digraph G {{
 graph [rankdir=LR, nodesep=0.3, ranksep=0.75, fontname="{FONT}", pad=0.2, compound=true, newrank=true];
 node [fontname="{FONT}", shape=box, style="rounded,filled", fillcolor="#FFFFFF", fontsize=11]; edge [fontname="{FONT}", fontsize=9];
 subgraph cluster_in {{ label="Capa de aplicación\\n(ilustrativa)"; style="rounded,filled"; fillcolor="#F3E5F5"; color="#6A1B9A"; fontsize=11;
   UC [label="Servicio de pedidos\\n(caso de uso:\\ncotizar envío)"]; }}
 subgraph cluster_core {{ label="Núcleo: lógica de logística"; style="rounded,filled"; fillcolor="#E8F5E9"; color="#1B5E20"; fontsize=11;
   LS [label="LogisticaService\\n(Client)"]; P [label="«puerto de salida»\\nServicioEnvio\\n(Target)", fillcolor="#E3F2FD", color="#0D47A1"]; }}
 subgraph cluster_ad {{ label="Adaptadores de infraestructura\\n(patrón Adapter)"; style="rounded,filled"; fillcolor="#FFF3E0"; color="#E65100"; fontsize=11;
   AL [label="Proveedor local\\n(ya conforme)"]; AR [label="RapidExpressAdapter"]; AA [label="AndinaCargoAdapter"]; AN [label="Adapter N…\\n(futuros operadores)", style="rounded,dashed,filled"]; }}
 subgraph cluster_ext {{ label="Sistemas externos\\n(no modificables)"; style="rounded,filled"; fillcolor="#F5F5F5"; color="#555555"; fontsize=11;
   EL [label="API proveedor local"]; ER [label="RapidExpressAPI"]; EA [label="AndinaCargoAPI"]; EN [label="API operador N", style="rounded,dashed,filled"]; }}
 UC -> LS [label=" invoca"];
 LS -> P [label=" depende de la\\l abstracción\\l"];
 P -> AL [dir=back, arrowtail=empty, style=dashed]; P -> AR [dir=back, arrowtail=empty, style=dashed]; P -> AA [dir=back, arrowtail=empty, style=dashed]; P -> AN [dir=back, arrowtail=empty, style=dashed];
 AL -> EL; AR -> ER; AA -> EA; AN -> EN [style=dashed];
}}'''
render(d3,'fig3_arquitectura')
