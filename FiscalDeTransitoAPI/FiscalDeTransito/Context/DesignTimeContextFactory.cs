using Microsoft.EntityFrameworkCore;
using Microsoft.EntityFrameworkCore.Design;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace FiscalDeTransitoApi.Context
{
    public class DesignTimeContextFactory //: IDesignTimeDbContextFactory<ApplicationDbContext>
    {
        public ApplicationDbContext CreateDbContext(string[] args)
        {
            var options = new DbContextOptionsBuilder();
            options.UseSqlServer(ApplicationDbContext.GetConnection());
            var context = new ApplicationDbContext(options.Options);
            return context;
        }
    }
}
