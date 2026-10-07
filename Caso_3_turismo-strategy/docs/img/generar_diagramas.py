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



# ---------- Figura 1: diseño original ----------
d1=f'''digraph G {{
 graph [rankdir=TB, nodesep=0.5, ranksep=0.65, fontname="{FONT}", pad=0.15];
 node [fontname="{FONT}"]; edge [fontname="{FONT}", fontsize=10];
 P [{cls("Proceso de compra", stereo="cliente (no entregado en el caso)", meths=["+ comprar(...)"], fill="#F5F5F5")}];
 A [{cls("CalculadorDescuento", attrs=["(sin atributos)"], meths=["+ calcular(tipo : String,","    valorCompra : double) : double","  if tipo == \"FRECUENTE\"       -> 0.10","  else if \"TEMPORADA_BAJA\"    -> 0.15","  else if \"CONVENIO\"          -> 0.20","  otherwise                    -> 0"], fill="#FDECEA", border="#B71C1C")}];
 P -> A [arrowhead=vee, label="  usa"];
 N [shape=note, style=filled, fillcolor="#FFF8E1", color="#8D6E00", fontsize=10, label="Mercadeo anuncia 5 tipos nuevos de política\\l→ 5 ramas más dentro de calcular()\\l   (aniversario, regional, municipio,\\l    temporales, cajas de compensación)\\l"];
 A -> N [style=dotted, arrowhead=none, color="#8D6E00"];
}}'''
render(d1,'fig1_diseno_original')

# ---------- Figura 2: UML Strategy adaptado ----------
d2=f'''digraph G {{
 graph [rankdir=TB, nodesep=0.35, ranksep=0.55, fontname="{FONT}", pad=0.15, newrank=true];
 node [fontname="{FONT}"]; edge [fontname="{FONT}", fontsize=10];
 CFG [{cls("ConfiguracionDescuentos", stereo="raíz de composición", meths=["+ crearCatalogo() :","    CatalogoPoliticas"], fill="#F3E5F5", border="#6A1B9A")}];
 PC [{cls("ProcesadorCompra", stereo="proceso principal", attrs=["- calculador"], meths=["+ procesar(tipoDescuento,","    valorCompra) : ResumenCompra"], fill="#FFFFFF", border="#555555")}];
 C [{cls("CalculadorDescuento", stereo="Context", attrs=["- catalogo : CatalogoPoliticas"], meths=["+ calcular(tipo : String,","    valorCompra : double) : double"], fill="#E8F5E9", border="#1B5E20")}];
 K [{cls("CatalogoPoliticas", stereo="registro de estrategias", attrs=["- politicas : Map<String,","    PoliticaDescuento>"], meths=["+ registrar(politica)","+ retirar(codigo) : boolean","+ buscar(codigo) : Optional"], fill="#FFFFFF", border="#555555")}];
 S [{cls("PoliticaDescuento", stereo="Strategy (interface)", meths=["+ codigo() : String","+ calcular(valorCompra :","    double) : double"], italic=True, fill="#E3F2FD", border="#0D47A1")}];
 P1 [{cls("DescuentoPorcentual", stereo="ConcreteStrategy", attrs=["- codigo : String","- porcentaje : double"], meths=["+ calcular(valorCompra)","  valorCompra * porcentaje"], fill="#FFF3E0", border="#E65100")}];
 P2 [{cls("DescuentoAniversario", stereo="ConcreteStrategy (nuevo requisito)", attrs=["- porcentaje, desde, hasta","- reloj : Clock"], meths=["+ calcular(valorCompra)","  solo si hoy está en la vigencia"], fill="#FFF3E0", border="#E65100")}];
 PC -> C [arrowhead=vee, label="  usa"];
 C -> K [arrowhead=vee, label="  consulta"];
 K -> S [dir=back, arrowtail=odiamond, arrowhead=none, label="  0..*"];
 S -> P1 [dir=back, arrowtail=empty, style=dashed];
 S -> P2 [dir=back, arrowtail=empty, style=dashed];
 CFG -> K [arrowhead=vee, style=dashed, color="#6A1B9A", fontcolor="#6A1B9A", label="«create»"];
 {{rank=same; CFG; PC; C}}
 {{rank=same; K; S}}
 {{rank=same; P1; P2}}
}}'''
render(d2,'fig2_uml_strategy')

# ---------- Figura 3: ubicación arquitectónica ----------
d3=f'''digraph G {{
 graph [rankdir=TB, nodesep=0.25, ranksep=0.5, fontname="{FONT}", pad=0.15, newrank=true];
 node [fontname="{FONT}", shape=box, style="rounded,filled", fillcolor="#FFFFFF", fontsize=12]; edge [fontname="{FONT}", fontsize=10];
 subgraph cluster_app {{ label="Capa de aplicación"; style="rounded,filled"; fillcolor="#F3E5F5"; color="#6A1B9A"; fontsize=12;
   PC [label="ProcesadorCompra\\n(proceso principal de compra)"]; }}
 subgraph cluster_dom {{ label="Dominio: reglas de descuento"; style="rounded,filled"; fillcolor="#E8F5E9"; color="#1B5E20"; fontsize=12;
   CA [label="CalculadorDescuento\\n(Context)"]; PD [label="PoliticaDescuento\\n(Strategy)", fillcolor="#E3F2FD", color="#0D47A1"];
   P1 [label="Descuento-\\nPorcentual", style="rounded,filled", fillcolor="#FFF3E0", color="#E65100"]; P2 [label="Descuento-\\nAniversario", fillcolor="#FFF3E0", color="#E65100"]; PN [label="Políticas N\\n(municipio, cajas…)", style="rounded,dashed,filled", fillcolor="#FFF3E0", color="#E65100"]; }}
 subgraph cluster_cfg {{ label="Composición y configuración (punto de cambio de Mercadeo)"; style="rounded,filled"; fillcolor="#F5F5F5"; color="#555555"; fontsize=12;
   CF [label="ConfiguracionDescuentos\\n+ CatalogoPoliticas"]; }}
 PC -> CA [label=" invoca"];
 CA -> PD [label=" depende de la abstracción"];
 PD -> P1 [dir=back, arrowtail=empty, style=dashed]; PD -> P2 [dir=back, arrowtail=empty, style=dashed]; PD -> PN [dir=back, arrowtail=empty, style=dashed];
 CF -> P1 [style=dotted, label=" registra"]; CF -> P2 [style=dotted]; CF -> PN [style=dotted];
 {{rank=same; P1; P2; PN}}
}}'''
render(d3,'fig3_arquitectura')
