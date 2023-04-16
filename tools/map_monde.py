import dataclasses
import enum
import math
from data_monde import SEGMENTS, LAND_CITIES, HARBOR_CITIES, LAND_NAMES, HARBOR_NAMES

SEG_LENGTH = 40
MAP_WIDTH = 1920

class Color(enum.Enum):
    NOIR = "Noir"
    BLANC = "Blanc"
    ROUGE = "Rouge"
    VERT = "Vert"
    JAUNE = "Jaune"
    VIOLET = "Violet"
    GRIS = "Gris"


class Type(enum.Enum):
    WAGON = "wagon"
    BATEAU = "bateau"
    DOUBLE = "double"


@dataclasses.dataclass
class City:
    name: str
    x: int
    y: int
    is_harbor: bool

    def to_java(self):
        return f'villes.put("{self.name}", new Ville("{self.name}", {"true" if self.is_harbor else "false"}));'
    
    def to_js(self):
        return f'"{self.name}": {{nom: "{self.name}", x: {self.x}, y: {self.y}, port: {"true" if self.is_harbor else "false"}}},'


@dataclasses.dataclass
class Segment:
    x: int
    y: int
    angle: float
    color: Color
    type: Type

    def start_point(self):
        ux = math.cos(self.angle)
        uy = math.sin(self.angle)
        return (self.x - SEG_LENGTH / 2 * ux, self.y - SEG_LENGTH / 2 * uy)

    def end_point(self):
        ux = math.cos(self.angle)
        uy = math.sin(self.angle)
        return (self.x + SEG_LENGTH / 2 * ux, self.y + SEG_LENGTH / 2 * uy)


class Route:
    def __init__(self, city1, city2, segments):
        self.city1, self.city2 = sorted((city1, city2), key=lambda city: city.name)
        self.segments = segments
        self.name = None
        self.color = self.segments[0].color
        self.type = self.segments[0].type
    
    def to_java(self):
        if self.type == Type.WAGON:
            return f"routes.add(new RouteTerrestre(villes.get(\"{self.city1.name}\"), villes.get(\"{self.city2.name}\"), Couleur.{self.color.name}, {len(self.segments)})); // {self.name}"
        elif self.type == Type.BATEAU:
            return f"routes.add(new RouteMaritime(villes.get(\"{self.city1.name}\"), villes.get(\"{self.city2.name}\"), Couleur.{self.color.name}, {len(self.segments)})); // {self.name}"
        else:
            return f"routes.add(new RoutePaire(villes.get(\"{self.city1.name}\"), villes.get(\"{self.city2.name}\"), {len(self.segments)})); // {self.name}"

    def to_js(self):
        lines = [f'"{self.name}": {{nom: "{self.name}", ville1: "{self.city1.name}", ville2: "{self.city2.name}", couleur: "{self.color.name.lower()}", type: "{self.type.name.lower()}", segments: [']
        for segment in self.segments:
            lines.append(f"  {{x: {segment.x}, y: {segment.y}, angle: {180 * segment.angle / math.pi}}},")
        lines.append("]},")
        return "\n".join(lines)


def distance(x1, y1, x2, y2):
    return min(
        math.sqrt((x1 - x2) ** 2 + (y1 - y2) ** 2),
        math.sqrt((x1 - x2 - MAP_WIDTH) ** 2 + (y1 - y2) ** 2)
    )


def closest_city(x, y, cities):
    min_dist = float("inf")
    closest_city = None
    for city in cities:
        dist = distance(city.x, city.y, x, y)
        if dist < min_dist:
            min_dist = dist
            closest_city = city
    return closest_city, min_dist


cities = []
for i in range(len(LAND_CITIES)):
    cities.append(
        City(
            LAND_NAMES[i],
            int(round(LAND_CITIES[i][0])),
            int(round(LAND_CITIES[i][1])),
            False,
        )
    )
for i in range(len(HARBOR_CITIES)):
    cities.append(
        City(
            HARBOR_NAMES[i],
            int(round(HARBOR_CITIES[i][0])),
            int(round(HARBOR_CITIES[i][1])),
            True,
        )
    )

segments = []
for s in SEGMENTS:
    segments.append(
        Segment(
            int(round(s[0])),
            int(round(s[1])),
            s[2],
            Color(s[3]),
            Type(s[4]),
        )
    )


def make_route(segments, cities):
    route_segments = []
    segment = segments.pop(0)
    city1, _ = closest_city(*segment.start_point(), cities)
    route_segments.append(segment)

    while len(segments) > 0:
        segment = segments[0]
        previous_segment = route_segments[-1]
        city2, dist_to_city = closest_city(*previous_segment.end_point(), cities)
        if (segment.color != previous_segment.color 
            or segment.type != previous_segment.type
            or distance(*previous_segment.end_point(), *segment.start_point()) >= dist_to_city):
                return Route(city1, city2, route_segments), segments
        else:
            route_segments.append(segment)
            previous_segment = segment
            segments.pop(0)
    city2, dist_to_city = closest_city(*previous_segment.end_point(), cities)
    return Route(city1, city2, route_segments), segments

routes = []
while len(segments) > 0:
    route, segments = make_route(segments, cities)
    routes.append(route)
routes.sort(key=lambda route: route.city2.name)
routes.sort(key=lambda route: route.city1.name)
for i, route in enumerate(routes):
    route.name = f"R{i+1}"



def print_js():
    print("export const villesData = {")
    for city in cities:
        print(city.to_js())
    print("};")
    print()

    print("export const routesData = {")
    for i, route in enumerate(routes):
        print(route.to_js())
    print("};")

def print_java():
    print("static public Plateau makePlateauMonde() {")
    print("Map<String, Ville> villes = new HashMap<>();")
    for city in cities:
        print(city.to_java())
    print()
    print("ArrayList<Route> routes = new ArrayList<>();")
    for route in routes:
        print(route.to_java())
    print("return new Plateau(new ArrayList<>(villes.values()), routes);")
    print("}")

# print_js()
print_java()